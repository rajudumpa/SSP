/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
Ext.define('Ssp.store.reference.CaseloadActions', {
    extend: 'Ext.data.Store',
    model: 'Ssp.model.reference.AbstractReference',
    mixins: [ 'Deft.mixin.Injectable' ],
    inject: {
        authenticatedPerson: 'authenticatedPerson',
        textStore:'sspTextStore'
    },
    constructor: function(){
        var me=this;
        me.callParent( arguments );
        Ext.apply(this, { proxy: '',
            autoLoad: false });
        return me;
    },
    load: function() {
        var me = this;
        if(me.getCount() < 1) {
            if(me.authenticatedPerson.hasAnyBulkExportPermissions()) {
                me.add({id: "EXPORT", name: me.textStore.getValueByCode('ssp.bulk-action.export-to-csv','Export to CSV')});
            }
            if(me.authenticatedPerson.hasAccess('BULK_EMAIL_ACTION')) {
                me.add({id: "EMAIL", name: me.textStore.getValueByCode('ssp.bulk-action.send-email', 'Send Email')});
            }
            if(me.authenticatedPerson.hasAccess('BULK_PROGRAM_STATUS_ACTION')) {
                me.add({id: "PROGRAM_STATUS_ACTIVE", name: me.textStore.getValueByCode('ssp.bulk-action.set-active-status',"Set 'Active' Status")});
            }
            if(me.authenticatedPerson.hasAccess('BULK_PROGRAM_STATUS_ACTION')) {
                me.add({id: "PROGRAM_STATUS_INACTIVE", name: me.textStore.getValueByCode('ssp.bulk-action.set-inactive-status', "Set 'Inactive' Status")});
            }
            if(me.authenticatedPerson.hasAccess('BULK_PROGRAM_STATUS_ACTION')) {
                me.add({id: "PROGRAM_STATUS_NON_PARTICIPATING", name: me.textStore.getValueByCode('ssp.bulk-action.set-non-participating-status', "Set 'Non-participating' Status")});
            }
            if(me.authenticatedPerson.hasAccess('BULK_PROGRAM_STATUS_ACTION')) {
                me.add({id: "PROGRAM_STATUS_NO_SHOW", name: me.textStore.getValueByCode('ssp.bulk-action.set-no-show-status', "Set 'No-Show' Status")});
            }
            if(me.authenticatedPerson.hasAccess('BULK_WATCH_ACTION')) {
                me.add({id: "WATCH", name: me.textStore.getValueByCode('ssp.bulk-action.watch','Watch')});
            }
            if(me.authenticatedPerson.hasAccess('BULK_WATCH_ACTION')) {
                me.add({id: "UNWATCH", name: me.textStore.getValueByCode('ssp.bulk-action.unwatch','Unwatch')});
            }
            if (me.authenticatedPerson.hasAccess('EXPORT_CUSTOMIZABLE_SEARCH_TO_CSV_ACTION')) {
                me.add({id: "CUSTOM_EXPORT", name: me.textStore.getValueByCode('ssp.bulk-action.custom-export-to-csv','Custom Export to CSV')});
            }
        }
        return me;
    }
});
