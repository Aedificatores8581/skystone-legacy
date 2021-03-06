package org.aedificatores.libspeedydoge.VF;

import org.aedificatores.libspeedydoge.Universal.Math.Pose;
import org.aedificatores.libspeedydoge.Universal.Math.Vector2;
import org.aedificatores.libspeedydoge.Universal.UniversalFunctions;

/**
 * All objects that sum to create the VectorField's output vector are VectorFieldComponents
 */
public abstract class VectorFieldComponent implements ActivatableComponent {
    //At strength inches away from the field's origin, the waypoint vector and the output vector are equally weighted
    public double strength,
            //Controls the concavity (steepness) of the magnitude of the curve as the distance away from the origin decreases
                falloff;
    //the origin point of the field
    public Pose location;
    //is the field active or not?
    private boolean isActive = true;
    //the destination point of a given path
    public Pose target;

    public VectorFieldComponent (Pose location, double strength, double falloff) {
        this.strength = strength;
        this.falloff = falloff;
        this.location = location;
    }

    /*
    returns the magnitude of a vector whose origin is a distance d away from the field's origin
    */
    public double getStrength (double d) {
        d = UniversalFunctions.clamp(0, d, d+1);
        return strength / d * Math.pow(Math.E, falloff * (strength - d));
    }
    /*
    generates a vector that points location in a desired direction
     */
    public abstract Vector2 interact(Pose position);
    /*
    sets target
     */
    public void setTarget(Pose point) {
        target = point;
        location.angle = Math.atan2(point.y - location.y, point.x - location.x);
    }

    /*
    returns target
     */
    public Pose getTarget(){
        return target;
    }
    public void activate(){
        isActive = true;
    }
    public void deactivate(){
        isActive = false;
    }
    public boolean isActive(){
        return isActive;
    }
}
