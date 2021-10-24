package de.dertoaster.classichostiles.entity;

import de.dertoaster.classichostiles.util.reflection.ReflectionMethod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;

public interface ISizable {

	//Now, let's proceed to some hackery... (Just some dirty access to entity fields)
	public default float getWidth(Pose pose) {
		if(this instanceof Entity) {
			return ((Entity)this).getDimensions(pose).width;
		}
		return 0F;
	}
	
	public default float getHeight(Pose pose) {
		if(this instanceof Entity) {
			return ((Entity)this).getDimensions(pose).height;
		}
		return 0F;
	}
	
	public default float getStepHeight() {
		if(this instanceof Entity) {
			return ((Entity)this).maxUpStep;
		}
		return 0F;
	}
	
	public default void setStepHeight(float value) {
		if(this instanceof Entity) {
			((Entity)this).maxUpStep = value;
		}
	}

	//Used to acquire the default size of the entity
	public float getDefaultWidth();
	public float getDefaultHeight();
	
	//Getter and setter for sizeScale field
	public float getSizeVariation();
	void applySizeVariation(float value);
	
	//wrapper for setSize cause interfaces don'T allow protected methods >:(
	static final ReflectionMethod<Object> METHOD_SET_SIZE = new ReflectionMethod<>(Entity.class, "func_70105_a", "setSize", Float.TYPE, Float.TYPE);
	
	//Access the setSize method of an entity
	default void hackSize(float w, float h) {
		if(this instanceof Entity) {
			METHOD_SET_SIZE.invoke((Entity)this, w, h);
		}
	}
	
	//This needs to be called in the implementing entity's constructor
	public default void initializeSize() {
		this.hackSize(this.getDefaultWidth(), this.getDefaultHeight());
	}

	//Always use this to change the size, never call resize directly!!
	public default void setSizeVariation(float size, Pose pose) {
		this.resize(size / this.getSizeVariation(), size / this.getSizeVariation(), pose);
		this.applySizeVariation(size);
	}
	//NEVER call this directly from the implementing class
	public default void resize(float widthScale, float heightSacle, Pose pose) {
		this.hackSize(this.getWidth(pose) * widthScale, this.getHeight(pose) * heightSacle);
		if (this.getStepHeight() * heightSacle >= 1.0) {
			this.setStepHeight(this.getStepHeight() * heightSacle);
		}
	}

	//These two methods NEED to be called on read/write entity to NBT!! OTherwise it won't get saved!
	default void callOnWriteToNBT(CompoundNBT compound) {
		compound.putFloat("sizeScaling", this.getSizeVariation());
	}
	default void callOnReadFromNBT(CompoundNBT compound) {
		this.setSizeVariation(compound.contains("sizeScaling") ? compound.getFloat("sizeScaling") : 1.0F, Pose.STANDING);
	}
	
}
