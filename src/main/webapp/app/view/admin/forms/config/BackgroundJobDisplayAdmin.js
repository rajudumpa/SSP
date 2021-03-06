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
Ext.define('Ssp.view.admin.forms.config.BackgroundJobDisplayAdmin', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.backgroundjobdisplayadmin',
    title: '',
    mixins: ['Deft.mixin.Injectable', 'Deft.mixin.Controllable'],
    controller: 'Ssp.controller.admin.config.BackgroundJobDisplayAdminViewController',
    inject: {
        apiProperties: 'apiProperties',
        authenticatedPerson: 'authenticatedPerson',
        columnRendererUtils: 'columnRendererUtils'
    },
    height: '100%',
    width: '100%',
    
    initComponent: function(){
        var me = this;
        Ext.apply(me, {
            viewConfig: {},
            autoScroll: true,
            selType: 'rowmodel',
            enableDragDrop: false,
			cls: 'configgrid',
            columns: [{
                header: 'Name',
                dataIndex: 'displayName',
                flex: 0.30
            }, {
                header: 'Description',
                dataIndex: 'description',
                flex: 0.70
            }],
             dockedItems: [{
                xtype: 'toolbar',
                dock: 'top',
                items: [{
                    xtype: 'label',
                    text: 'Double-click on a Background Job to view Details or Execute Job.  ' +
                    '  Please note some jobs aren\'t executable while some jobs may slow the system and take several hours to finish.'
                }]
            }]
        });
        
        return me.callParent(arguments);
    }
});