/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.picketlink.as.console.client.ui.federation.idp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.as.console.client.shared.model.DeploymentRecord;
import org.picketlink.as.console.client.PicketLinkConsoleFramework;
import org.picketlink.as.console.client.shared.subsys.model.FederationWrapper;
import org.picketlink.as.console.client.shared.subsys.model.IdentityProvider;
import org.picketlink.as.console.client.shared.subsys.model.IdentityProviderWrapper;
import org.picketlink.as.console.client.shared.subsys.model.TrustDomain;
import org.picketlink.as.console.client.ui.federation.AbstractFederationDetailEditor;
import org.picketlink.as.console.client.ui.federation.FederationPresenter;
import org.picketlink.as.console.client.ui.federation.Wizard;

import com.google.gwt.user.client.ui.TabPanel;

/**
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 * @since Mar 30, 2012
 */
public class IdentityProviderEditor extends AbstractFederationDetailEditor<IdentityProvider> {

    private TrustedDomainTabEditor trustedDomainTabEditor;
    private SignatureSupportTabEditor signatureSupportTabEditor;

    public IdentityProviderEditor(FederationPresenter presenter) {
        super(presenter, new IdentityProviderTable(presenter), IdentityProvider.class);
    }

    /* (non-Javadoc)
     * @see org.picketlink.as.console.client.ui.federation.AbstractFederationDetailEditor#getEntityName()
     */
    @Override
    public String doGetEntityName() {
        return PicketLinkConsoleFramework.CONSTANTS.common_label_identityProvider();
    }
    
    /* (non-Javadoc)
     * @see org.picketlink.as.console.client.ui.federation.AbstractFederationDetailEditor#doGetDescription()
     */
    @Override
    protected String doGetDescription() {
        return PicketLinkConsoleFramework.CONSTANTS.subsys_picketlink_identity_provider_desc();
    }
    
    /* (non-Javadoc)
     * @see org.picketlink.as.console.client.ui.federation.AbstractFederationDetailEditor#getStackName()
     */
    @Override
    public String doGetTableSectionName() {
        return "Identity Providers";
    }

    @Override
    protected boolean doInsert(IdentityProvider identityProvider) {
        if (identityProvider.isExternal()) {
            identityProvider.setName(getFederation().getName() + "-" + "external-idp");
        }
        
        getPresenter().getFederationManager().onCreateIdentityProvider(identityProvider);
        
        if (!identityProvider.isExternal()) {
            getPresenter().getDeploymentManager().restartIdentityProvider(identityProvider);
        }
        
        return true;
    }

    /* (non-Javadoc)
     * @see org.picketlink.as.console.client.ui.federation.AbstractFederationDetailEditor#addTabs(com.google.gwt.user.client.ui.TabPanel)
     */
    @Override
    protected void addDetailsSectionTabs(TabPanel bottomTabs) {
        bottomTabs.add(getSignatureSupportTabEditor().asWidget(), "Signature Support");
        bottomTabs.add(getTrustedDomainTabEditor().asWidget(), "Trusted Domains");
    }

    /**
     * @return
     */
    private TrustedDomainTabEditor getTrustedDomainTabEditor() {
        if (this.trustedDomainTabEditor == null) {
            this.trustedDomainTabEditor = new TrustedDomainTabEditor(getPresenter());
        }

        return this.trustedDomainTabEditor;
    }

    private SignatureSupportTabEditor getSignatureSupportTabEditor() {
        if (this.signatureSupportTabEditor == null) {
            this.signatureSupportTabEditor = new SignatureSupportTabEditor(getPresenter());
        }

        return this.signatureSupportTabEditor;
    }

    /* (non-Javadoc)
     * @see org.picketlink.as.console.client.ui.federation.AbstractFederationDetailEditor#doUpdateSelection(org.picketlink.as.console.client.shared.subsys.model.GenericFederationEntity)
     */
    @Override
    protected void doUpdateSelection(IdentityProvider policy) {
        getTrustedDomainTabEditor().setIdentityProvider(policy);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.picketlink.as.console.client.ui.federation.idp.AbstractFederationDetailEditor#onDelete(org.picketlink.as.console.
     * client.shared.subsys.model.GenericFederationEntity)
     */
    @Override
    protected void doDelete(IdentityProvider identityProvider) {
        this.getPresenter().getFederationManager().onRemoveIdentityProvider(identityProvider);
        enableAddButton();
    }

    /* (non-Javadoc)
     * @see org.picketlink.as.console.client.ui.federation.AbstractFederationDetailEditor#doUpdate(org.picketlink.as.console.client.shared.subsys.model.GenericFederationEntity, java.util.Map)
     */
    public void doUpdate(IdentityProvider identityProvider, Map<String, Object> changedValues) {
        this.getPresenter().getFederationManager().onUpdateIdentityProvider(identityProvider, changedValues);
        if (identityProvider.getName().indexOf("external") == -1) {
            this.getPresenter().getDeploymentManager().restartIdentityProvider(identityProvider);
        }
    }

    @Override
    public Wizard<IdentityProvider> doCreateWizard() {
        return new NewIdentityProviderWizard(this, getEntityClass(), getPresenter(), "identity-provider");
    }

    /**
     * @param name
     * @param identityProviders
     * @param resourceExists
     */
    public void updateIdentityProviders(FederationWrapper federation) {
        List<IdentityProvider> identityProviders = new ArrayList<IdentityProvider>();
        
        for (IdentityProviderWrapper identityProviderWrapper : federation.getIdentityProviders()) {
            identityProviders.add(identityProviderWrapper.getIdentityProvider());
        }
        
        setData(federation, identityProviders);
        
        // disables the add button since we already have a idp configuration
        if (!identityProviders.isEmpty()) {
            disableAddButton();
        } else {
            enableAddButton();
        }
        
        if (!identityProviders.isEmpty()) {
            getSignatureSupportTabEditor().setIdentityProvider(identityProviders.get(0));
        }
        
        updateTrustedDomains(federation);
    }

    /**
     * @param federation
     */
    private void updateTrustedDomains(FederationWrapper federation) {
        List<TrustDomain> trustDomains = new ArrayList<TrustDomain>();
        
        for (IdentityProviderWrapper identityProvider : federation.getIdentityProviders()) {
            trustDomains.addAll(identityProvider.getTrustDomains());
        }

        this.getTrustedDomainTabEditor().getTrustDomainTable().getDataProvider().setList(trustDomains);
    }

    public void updateDeployments(List<DeploymentRecord> deployments) {
        if (getWizard() != null) {
            ((NewIdentityProviderWizard) getWizard()).updateAliasItems();
        }
    }

}