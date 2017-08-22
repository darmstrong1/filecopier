package net.sf.fc.gui.c;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.fc.gui.m.Model;
import net.sf.fc.gui.v.View;

public class BaseController implements Controller {

    private final List<View> registeredViews;
    private final List<Model> registeredModels;

    /** Creates a new instance of Controller */
    public BaseController() {
        registeredViews = new ArrayList<View>();
        registeredModels = new ArrayList<Model>();
    }

    //  Used to observe property changes from registered models and propagate
    //  them on to all the views.

    /**
     * This method is used to implement the PropertyChangeListener interface. Any model
     * changes will be sent to this controller through the use of this method.
     * @param evt An object that describes the model's property change.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (View view: registeredViews) {
            view.modelPropertyChange(evt);
        }
    }

    @Override
    public void addModel(Model model) {
        registeredModels.add(model);
        model.addPropertyChangeListener(this);
    }

    @Override
    public void removeModel(Model model) {
        registeredModels.remove(model);
        model.removePropertyChangeListener(this);
    }

    @Override
    public void addView(View view) {
        registeredViews.add(view);
    }

    @Override
    public void removeView(View view) {
        registeredViews.remove(view);
    }

    /**
     * Convenience method that subclasses can call upon to fire off property changes
     * back to the models. This method used reflection to inspect each of the model
     * classes to determine if it is the owner of the property in question. If it
     * isn't, a NoSuchMethodException is throws (which the method ignores).
     *
     * @param propertyName The name of the property
     * @param newValue An object that represents the new value of the property.
     */
    protected void setModelProperty(String propertyName, Object newValue) {

        for (Model model: registeredModels) {
            try {
                Method method = getModelMethod(model, propertyName, newValue.getClass());
                if(method != null) method.invoke(model, newValue);
            } catch (Throwable t) {
                //  TODO:Handle exception
                t.printStackTrace();
            }
        }
    }

    protected Method getModelMethod(Model model, String propertyName, Class<?> clazz) {

        try {
            return model.getClass().getMethod("set"+propertyName, clazz);

        } catch(NoSuchMethodException ignored) {
            // The method may be defined to accept an interface that the class implements, so
            // see if we can find it like that.
            Class<?>[] interfaces = clazz.getInterfaces();
            if(interfaces.length > 0) {
                for(Class<?> iface: interfaces) {
                    Method m = getModelMethod(model, propertyName, iface);
                    // Only return if m is not null. Otherwise, it won't get a chance to check the superclass.
                    if(m != null) return m;
                }
            }

            // Either the method did not accept the class or one of its interfaces as an argument. Check to see if the superclass or
            // one of its interfaces is the argument.
            Class<?> superClazz = clazz.getSuperclass();
            if(superClazz != null) {
                return getModelMethod(model, propertyName, superClazz);
            }
        } catch(Throwable t) {
            //  TODO:Handle exception
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Convenience method that subclasses can call upon to fire off property changes
     * back to the models. This method used reflection to inspect each of the model
     * classes to determine if it is the owner of the property in question. If it
     * isn't, a NoSuchMethodException is throws (which the method ignores). This one
     * is to get a property instead of setting one.
     *
     * @param propertyName The name of the property
     */
    protected void getModelProperty(String propertyName) {

        for (Model model: registeredModels) {
            try {
                Method method = model.getClass().getMethod("get"+propertyName, ( Class<?>[])null);
                if(method != null) method.invoke(model, (Object[])null);
            } catch (Throwable t) {
                //  TODO:Handle exception
                t.printStackTrace();
            }
        }
    }

}
