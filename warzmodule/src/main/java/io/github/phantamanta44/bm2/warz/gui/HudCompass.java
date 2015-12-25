package io.github.phantamanta44.bm2.warz.gui;

import io.github.phantamanta44.bm2.core.event.IListener;
import io.github.phantamanta44.bm2.core.util.Vector2;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import com.google.common.collect.Lists;

@SuppressWarnings("unchecked")
public class HudCompass implements IListener {
	
	private static final ResourceLocation COMPASS_LOC = new ResourceLocation("zcubed", "textures/gui/compass.png");
	private static final double TWOPI = Math.PI * 2;
	
	private Minecraft mc;

	public HudCompass(Minecraft mc) {
		this.mc = mc;
	}

	@ListenTo
	public void draw(RenderGameOverlayEvent.Post event) {
		if (event.type != ElementType.PORTAL)
			return;
		if (!this.mc.thePlayer.inventory.hasItem(Items.compass))
			return;
		if (!playersInRange())
			return;
		
		ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		int xBounds = sr.getScaledWidth();
		int yBounds = sr.getScaledHeight();
		
		Vector2<Double> a = new Vector2<>(this.mc.thePlayer.posX, this.mc.thePlayer.posZ);
		double facing = (double)(this.mc.thePlayer.rotationYaw + 90) % 360 * -(Math.PI / 180);
		BlockPos b = this.mc.theWorld.getSpawnPoint();
		double compBearing = clampRad(-Math.atan2(a.getY() - b.getZ(), a.getX() - b.getX()));
		double theta = -angleRange(compBearing - facing) + (Math.PI / 2);
		
		Vector2<Integer> origin = new Vector2<>(
				(int)(Math.cos(theta) * (xBounds * 0.45) + (xBounds / 2)),
				(int)(Math.sin(theta) * (yBounds * 0.45) + (yBounds / 2)));
		
		List<Vector2<Integer>> boxCorners = calculateCorners(origin, TWOPI - (theta + TWOPI + (Math.PI / 2)) % TWOPI);
		
		this.mc.renderEngine.bindTexture(COMPASS_LOC);
		
		GlStateManager.color(1, 1, 1, 0.5F);
		
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		wr.startDrawingQuads();
		wr.addVertexWithUV(boxCorners.get(0).
				getX(), boxCorners.get(0).getY(), -90.0F, 0, 1);
		wr.addVertexWithUV(boxCorners.get(1).getX(), boxCorners.get(1).getY(), -90.0F, 1, 1);
		wr.addVertexWithUV(boxCorners.get(2).getX(), boxCorners.get(2).getY(), -90.0F, 1, 0);
		wr.addVertexWithUV(boxCorners.get(3).getX(), boxCorners.get(3).getY(), -90.0F, 0, 0);
		tess.draw();
		
	}
	
	public double clampRad(double rad) {
		double r = rad % TWOPI;
		if (r >= 0)
			return r;
		return TWOPI + r; 
	}
	
	public double angleRange(double a) {
		double b = a % TWOPI;
		if (b < 0)
			b += TWOPI;
		return b;
	}
	
	public List<Vector2<Integer>> calculateCorners(Vector2<Integer> o, double rot) {
		List<Vector2<Integer>> ret = Lists.newArrayList(null, null, null, null);
		
		// POINT 3, 4
		int xOffset1 = (int)(18 * Math.cos(rot));
		int yOffset1 = (int)(18 * Math.sin(rot));
		ret.set(2, new Vector2<Integer>(o.getX() + xOffset1, o.getY() - yOffset1));
		ret.set(3, new Vector2<Integer>(o.getX() - xOffset1, o.getY() + yOffset1));
		
		// POINT 1, 2
		int xOffset = (int)(18 * Math.cos((Math.PI / 2) - rot));
		int yOffset = (int)(18 * Math.sin((Math.PI / 2) - rot));
		ret.set(0, addVecs(ret.get(3), xOffset, yOffset));
		ret.set(1, addVecs(ret.get(2), xOffset, yOffset));
		
		return ret;
	}
	
	public static Vector2<Integer> addVecs(Vector2<Integer> vec, int x, int y) {
		return new Vector2<Integer>(vec.getX() + x, vec.getY() + y);
	}
	
	public boolean playersInRange() {
		for (Object o : this.mc.theWorld.playerEntities) {
			if (o.equals(this.mc.thePlayer))
				continue;
			Vec3 ePos = ((EntityPlayer)o).getPositionVector();
			double x1 = ePos.xCoord;
			double x2 = this.mc.thePlayer.posX;
			double y1 = ePos.yCoord;
			double y2 = this.mc.thePlayer.posY;
			if (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) <= 150)
				return true;
		}
		return false;
	}
	
}
