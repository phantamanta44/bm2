package io.github.phantamanta44.bm2.etc;

import io.github.phantamanta44.bm2.core.BM2;
import io.github.phantamanta44.bm2.core.util.IFuture;
import io.github.phantamanta44.bm2.core.util.StreamUtils;
import io.github.phantamanta44.bm2.etc.NameLookup.NameHistory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class NameLookup implements IFuture<NameHistory> {

	private static final String ID_REQ = "https://api.mojang.com/users/profiles/minecraft/%s";
	private static final String HIST_REQ = "https://api.mojang.com/user/profiles/%s/names";
	
	private String name;
	private UUID id;
	private boolean done = false;
	private Consumer<NameHistory> callback;
	private NameHistory result;
	
	public NameLookup(String name) {
		this.name = name;
	}
	
	public NameLookup(UUID id) {
		this.id = id;
	}
	
	@Override
	public void dispatch() {
		if (this.id != null)
			this.requestHist(this.id.toString().replaceAll("-", ""));
		else if (this.name != null)
			this.requestId(this.name);
		else
			throw new IllegalStateException("Improperly constructed NameLookup!");
	}
	
	private void requestId(String name) {
		new Thread("bm2namelookup") {
			@Override
			public void run() {
				try {
					JsonParser parser = new JsonParser();
					BufferedReader in = new BufferedReader(new InputStreamReader(new URL(String.format(ID_REQ, name)).openStream()));
					JsonElement idResp = parser.parse(in);
					in.close();
					if (idResp.isJsonNull()) {
						finish(new NameEntry[0]);
						return;
					}
					requestHist(idResp.getAsJsonObject().get("id").getAsString());
				} catch (Exception e) {
					BM2.warn("Could not complete name lookup!");
					e.printStackTrace();
					finishNull();
				}
			}
		}.start();
	}
	
	private void requestHist(String id) {
		new Thread("bm2namelookup") {
			@Override
			public void run() {
				try {
					JsonParser parser = new JsonParser();
					BufferedReader in = new BufferedReader(new InputStreamReader(new URL(String.format(HIST_REQ, id)).openStream()));
					JsonArray histResp = parser.parse(in).getAsJsonArray();
					in.close();
					NameEntry[] hist = StreamUtils.streamify(histResp, histResp.size(), (h, i) -> h.get(i).getAsJsonObject())
						.map(j -> {
							long changeDate = j.has("changedToAt") ? j.get("changedToAt").getAsLong() : 0L;
							return new NameEntry(j.get("name").getAsString(), changeDate);
						})
						.toArray(len -> new NameEntry[len]);
					finish(hist);
				} catch (Exception e) {
					BM2.warn("Could not complete name lookup!");
					e.printStackTrace();
					finishNull();
				}
			}
		}.start();
	}
	
	private void finishNull() {
		this.result = null;
		if (this.callback != null)
			this.callback.accept(null);
		this.done = true;
	}
	
	private void finish(NameEntry[] resp) {
		this.result = new NameHistory(resp);
		if (this.callback != null)
			this.callback.accept(this.result);
		this.done = true;
	}
	
	@Override
	public boolean isDone() {
		return this.done;
	}

	@Override
	public NameHistory getResult() {
		return this.done ? this.result : null;
	}

	@Override
	public NameLookup promise(Consumer<NameHistory> callback) {
		this.callback = callback;
		return this;
	}
	
	public static class NameHistory {
		
		public final ImmutableList<NameEntry> history;
		
		public NameHistory(NameEntry[] entries) {
			this.history = ImmutableList.copyOf(entries);
		}
		
	}
	
	public static class NameEntry {
		
		public final String name;
		public final long changeDate;
		
		public NameEntry(String name, long ts) {
			this.name = name;
			this.changeDate = ts;
		}
		
	}

}
