package dev.fulmineo.companion_bats.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.fulmineo.companion_bats.CompanionBats;
import dev.fulmineo.companion_bats.entity.CompanionBatEntity;
import dev.fulmineo.companion_bats.entity.CompanionBatLevels;
import dev.fulmineo.companion_bats.entity.CompanionBatLevels.CompanionBatClassLevel;
import dev.fulmineo.companion_bats.item.CompanionBatArmorItem;
import dev.fulmineo.companion_bats.CompanionBatClass;
import dev.fulmineo.companion_bats.nbt.EntityData;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import net.minecraft.client.render.GameRenderer;

public class CompanionBatScreen extends HandledScreen<CompanionBatScreenHandler> {
	private static final Identifier TEXTURE = new Identifier(CompanionBats.MOD_ID, "textures/gui/container/bat.png");
	private int level;
	private int currentLevelExp;
	private int nextLevelExp;
	private int classLevel;
	private int currentClassLevelExp;
	private int nextClassLevelExp;
	private boolean maxExpReached;
	private boolean maxClassExpReached;
	private float currentHealth;
	private float maxHealth;
	private float attack;
	private float speed;
	private CompanionBatClass currentClass;
	private ItemStack armorStack;
	private PlayerInventory inventory;

   	public CompanionBatScreen(CompanionBatScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);

		this.inventory = inventory;
		ItemStack batItemStack = inventory.player.getStackInHand(handler.hand);
		EntityData.createIfMissing(batItemStack);
		EntityData entityData = new EntityData(batItemStack);

		this.setLevel(entityData);
		this.setClassLevel(entityData);
		this.setAttributes(entityData);

		this.passEvents = false;
   	}

	private void setLevel(EntityData entityData){
		this.level = CompanionBatLevels.getLevelByExp(entityData.getExp());

		if (this.level+1 < CompanionBatLevels.LEVELS.length){
			this.currentLevelExp = entityData.getExp() - CompanionBatLevels.LEVELS[this.level].totalExpNeeded;
			this.nextLevelExp = CompanionBatLevels.LEVELS[this.level+1].totalExpNeeded - CompanionBatLevels.LEVELS[this.level].totalExpNeeded;
		} else {
			this.currentLevelExp = entityData.getExp();
			this.nextLevelExp = CompanionBatLevels.LEVELS[this.level].totalExpNeeded;
			this.maxExpReached = this.currentLevelExp >= this.nextLevelExp;
		}
	}

	private void setClassLevel(EntityData entityData){
		this.armorStack = ItemStack.fromNbt((NbtCompound)entityData.getArmor());
		if (this.armorStack.getItem() instanceof CompanionBatArmorItem){
			this.currentClass = ((CompanionBatArmorItem)this.armorStack.getItem()).getBatClass();
			if (this.currentClass != null){
				int classExp = entityData.getClassExp(this.currentClass);
				this.classLevel = CompanionBatLevels.getClassLevelByExp(this.currentClass, classExp);
				CompanionBatClassLevel[] classLevels = CompanionBatLevels.CLASS_LEVELS.get(this.currentClass);
				if (this.classLevel+1 < classLevels.length){
					this.currentClassLevelExp = classExp - classLevels[this.classLevel].totalExpNeeded;
					this.nextClassLevelExp = classLevels[this.classLevel+1].totalExpNeeded - classLevels[this.classLevel].totalExpNeeded;
					this.maxClassExpReached = false;
				} else {
					this.currentClassLevelExp = classExp;
					this.nextClassLevelExp = classLevels[this.classLevel].totalExpNeeded;
					this.maxClassExpReached = this.currentClassLevelExp >= this.nextClassLevelExp;
				}
			}
		} else {
			this.currentClass = null;
		}
	}

	private void setAttributes(EntityData entityData){
		this.currentHealth = Math.round(entityData.getHealth() * 10F) / 10F;
		this.maxHealth = CompanionBatEntity.getLevelHealth(this.level);
		this.attack = CompanionBatEntity.getLevelAttack(this.level);
		this.speed = Math.round(CompanionBatEntity.getLevelSpeed(this.level) * 100F) / 100F;
	}

	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;

		// Draws the background
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

		// Draws the additional slots
		this.drawTexture(matrices, i + 7, j + 35 - 18, 0, this.backgroundHeight, 18, 18);
		this.drawTexture(matrices, i + 7, j + 35, 18, this.backgroundHeight, 18, 18);
		this.drawTexture(matrices, i + 7, j + 35 + 18, 36, this.backgroundHeight, 18, 18);

		float x = i + 28;
		float y = j + 20;
		int strideY = 10;
		int strideX = 59;

		// Draws the text
		this.textRenderer.draw(matrices, new TranslatableText("gui.companion_bats.bat.level"), x, y, 0xFFFFFFFF);
		this.textRenderer.draw(matrices, new TranslatableText("").append(""+(this.level + 1)).append(" [").append(this.maxExpReached ? new TranslatableText("gui.companion_bats.bat.max") : new TranslatableText("").append(this.currentLevelExp+" / "+this.nextLevelExp)).append("]"), x + strideX, y, 0xFFFFFFFF);

		y += strideY;

		if (this.currentClass != null){
			this.textRenderer.draw(matrices, new TranslatableText("gui.companion_bats.bat.class"), x, y, 0xFFFFFFFF);
			this.textRenderer.draw(matrices, new TranslatableText("gui.companion_bats.bat.class." + this.currentClass.toString()), x + strideX, y, 0xFFFFFFFF);

			y += strideY;

			this.textRenderer.draw(matrices, new TranslatableText("gui.companion_bats.bat.class_level"), x, y, 0xFFFFFFFF);
			this.textRenderer.draw(matrices, new TranslatableText("").append(""+(this.classLevel + 1)).append(" [").append(this.maxClassExpReached ? new TranslatableText("gui.companion_bats.bat.max") : new TranslatableText("").append(this.currentClassLevelExp+" / "+this.nextClassLevelExp)).append("]"), x + strideX, y, 0xFFFFFFFF);
		} else {
			y += strideY;
		}

		y += strideY * 2;

		strideX = 11;

		this.textRenderer.draw(matrices, "❤", x, y, 0xFFFFFFFF);
		int offset = this.textRenderer.draw(matrices, this.currentHealth+" / "+this.maxHealth, x + strideX, y, 0xFFFFFFFF) - (int)x;
		int speedOffset = 110 + (String.valueOf(this.speed).length() <= 3 ? 6 : 0);

		int digits = (String.valueOf(this.attack).length() - 2) * 3;
		offset += ((speedOffset - offset) / 2) - digits - 10;

		this.textRenderer.draw(matrices, "🗡", x + offset , y, 0xFFFFFFFF);
		this.textRenderer.draw(matrices, ""+this.attack, x + offset + strideX, y, 0xFFFFFFFF);

		this.textRenderer.draw(matrices, "⬈", x + speedOffset, y, 0xFFFFFFFF);
		this.textRenderer.draw(matrices, ""+this.speed, x + speedOffset + strideX - 2, y, 0xFFFFFFFF);
	}

	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		ItemStack batItemStack = this.inventory.player.getStackInHand(handler.hand);
		EntityData entityData = new EntityData(batItemStack);
		ItemStack armorStack = ItemStack.fromNbt(entityData.getArmor());
		if (!this.armorStack.getItem().equals(armorStack.getItem())) this.setClassLevel(entityData);
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
 }
