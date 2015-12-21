package io.github.phantamanta44.bm2.core.event;

import io.github.phantamanta44.bm2.core.event.IListener.ListenTo;
import io.github.phantamanta44.bm2.core.module.ModuleManager;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@SuppressWarnings("unchecked")
public class EventManager {

	private static final Map<Class<? extends IListener>, HandlerSignature> handlerSigMap = Maps.newHashMap();
	private static final List<IListener> handlers = Lists.newArrayList();
	
	public static void registerHandler(String moduleId, IListener handler) {
		handlers.add(handler);
		Class<? extends IListener> handlerClass = handler.getClass();
		if (!handlerSigMap.containsKey(handlerClass))
			handlerSigMap.put(handlerClass, new HandlerSignature(moduleId, handlerClass));
	}
	
	@SubscribeEvent
	public void acceptEvent(Event event) throws Exception {
		Class<? extends Event> eventType = event.getClass();
		for (IListener listener : handlers) {
			Method listenerMethod;
			HandlerSignature handlerSig = handlerSigMap.get(listener.getClass());
			if (ModuleManager.isEnabled(handlerSig.moduleId)
				&& (listenerMethod = handlerSig.listenerMethods.get(eventType)) != null) {
				listenerMethod.invoke(listener, event);
			}
		}
	}
	
	private static class HandlerSignature {
		
		public final String moduleId;
		public final Map<Class<? extends Event>, Method> listenerMethods = Maps.newHashMap();
		
		public HandlerSignature(String moduleId, Class<? extends IListener> listenerClass) {
			this.moduleId = moduleId;
			Method[] methods = listenerClass.getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(ListenTo.class)
					&& method.getParameterCount() == 1
					&& method.getParameterTypes()[0].isAssignableFrom(Event.class)) {
					Class<? extends Event> eventType = (Class<? extends Event>)method.getParameterTypes()[0];
					if (this.listenerMethods.containsKey(eventType))
						throw new IllegalStateException("Duplicate listener methods for event " + eventType.getName() + "!");
					this.listenerMethods.put(eventType, method);
				}
			}
		}
		
	}
	
}
