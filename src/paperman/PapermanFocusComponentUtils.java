 /* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paperman;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author artivisi
 */
public class PapermanFocusComponentUtils extends FocusTraversalPolicy {

    private List<Component> componentList = new ArrayList<Component>();

    public PapermanFocusComponentUtils(List<Component> componentList) {
        this.componentList.addAll(componentList);
    }

    @Override
    public Component getComponentAfter(Container aContainer, Component aComponent) {
        int idx = (componentList.indexOf(aComponent) + 1) % componentList.size();
        return componentList.get(idx);
    }

    @Override
    public Component getComponentBefore(Container aContainer, Component aComponent) {
        int idx = componentList.indexOf(aComponent) - 1;
        if (idx < 0) {
            idx = componentList.size() - 1;
        }
        return componentList.get(idx);
    }

    @Override
    public Component getFirstComponent(Container aContainer) {
        return componentList.get(0);
    }

    @Override
    public Component getLastComponent(Container aContainer) {
        return componentList.get(componentList.size() - 1);
    }

    @Override
    public Component getDefaultComponent(Container aContainer) {
        return componentList.get(0);
    }
}
