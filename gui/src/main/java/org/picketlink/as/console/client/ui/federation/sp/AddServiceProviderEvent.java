package org.picketlink.as.console.client.ui.federation.sp;

import org.picketlink.as.console.client.shared.subsys.model.ServiceProvider;

import com.google.gwt.event.shared.GwtEvent;

public class AddServiceProviderEvent extends GwtEvent<ChangedServiceProviderHandler> {
    
    public static Type<ChangedServiceProviderHandler> TYPE = new Type<ChangedServiceProviderHandler>();
    private final ServiceProvider ServiceProvider;
    
    public AddServiceProviderEvent(ServiceProvider ServiceProvider) {
        this.ServiceProvider = ServiceProvider;
    }
    
    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
     */
    @Override
    protected void dispatch(ChangedServiceProviderHandler handler) {
        handler.onAddServiceProvider(this.ServiceProvider);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ChangedServiceProviderHandler> getAssociatedType() {
        return TYPE;
    }
    
}