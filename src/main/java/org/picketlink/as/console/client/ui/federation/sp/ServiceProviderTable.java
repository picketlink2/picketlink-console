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

package org.picketlink.as.console.client.ui.federation.sp;

import org.jboss.as.console.client.Console;
import org.picketlink.as.console.client.shared.subsys.model.ServiceProvider;
import org.picketlink.as.console.client.ui.federation.AbstractModelElementTable;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;

/**
 * <p>
 * A table widget to be used to show the trusted domains.
 * </p>
 * 
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 * @since Mar 19, 2012
 */
public class ServiceProviderTable extends AbstractModelElementTable<ServiceProvider> {

    /*
     * (non-Javadoc)
     * 
     * @see org.picketlink.as.console.client.ui.federation.AbstractModelElementTable#doGetKey(java.lang.Object)
     */
    @Override
    protected Object doGetKey(ServiceProvider item) {
        return item.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.picketlink.as.console.client.ui.federation.AbstractModelElementTable#doAddConlumns(org.jboss.ballroom.client.widgets
     * .tables.DefaultCellTable)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doAddConlumns(CellTable table) {
        TextColumn<ServiceProvider> aliasColumn = new TextColumn<ServiceProvider>() {
            @Override
            public String getValue(ServiceProvider record) {
                return record.getName();
            }
        };

        table.addColumn(aliasColumn, Console.CONSTANTS.common_label_name());
    }

}