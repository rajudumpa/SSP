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
Ext.define('Ssp.view.tools.map.MAPView', {
    extend: 'Ext.form.Panel',
    alias: 'widget.mapview',
    mixins: ['Deft.mixin.Injectable', 'Deft.mixin.Controllable'],
    controller: 'Ssp.controller.tool.map.MAPViewController',
    inject:{
		currentMapPlan: 'currentMapPlan',
        authenticatedPerson: 'authenticatedPerson',
        textStore: 'sspTextStore'
    },	
    width: '100%',
    height: '100%',
  initComponent: function(){
        var me = this;
        Ext.apply(me, {
            layout: {
                type: 'fit'
            },
            autoScroll: true,
            padding: 0,
            preventHeader: true,
            border: 0,
            margin: '0 0 0 0 ',
            minWidth: '500',
            items: [{
    			xtype: 'semesterpanelcontainer',
    			flex:1
            }
            ],
            dockedItems: [{
                dock: 'top',
                xtype: 'toolbar',
				height: '25',
                items: [{
                    tooltip: me.textStore.getValueByCode('ssp.tooltip.new-plan-button','Create New Plan'),
                    text: '<u>' + me.textStore.getValueByCode('ssp.label.new-plan-button','New Plan') + '</u>',
                    //width: 30,
                    height: 22,
                   // cls: 'emailIcon',
                    xtype: 'button',
                    itemId: 'createNewPlanButton'
                }, {
                    tooltip: me.textStore.getValueByCode('ssp.tooltip.load-saved-plan-button','Load Saved Plan'),
                    text: '<u>' + me.textStore.getValueByCode('ssp.label.load-saved-plan-button','Load Saved Plan') + '</u>',
                    height: 22,
                    xtype: 'button',
	                hidden: !me.authenticatedPerson.hasAccess('MAP_TOOL_LOAD_BUTTON'),
                    itemId: 'loadSavedPlanButton'
                },
				{
                    tooltip: me.textStore.getValueByCode('ssp.tooltip.load-template-button','Load Template'),
                    text: '<u>' + me.textStore.getValueByCode('ssp.label.load-template-button','Load Template') + '</u>',
                    hidden: !me.authenticatedPerson.hasAccess('MAP_TOOL_LOAD_BUTTON'),
                    height: 22,
                    xtype: 'button',
                    itemId: 'loadTemplateButton'
                },
                {
                    xtype: 'button',
                    text: me.textStore.getValueByCode('ssp.label.save-button','Save'),
                    itemId: 'addTool',
                    height: 22,
                    hidden: !me.authenticatedPerson.hasAccess('MAP_TOOL_SAVE_BUTTON'),
                    menu: {
                    items: [{
                            xtype: 'button',
                            text: me.textStore.getValueByCode('ssp.label.save-plan-button','Save Plan'),
                            itemId: 'savePlanButton',
                            hidden: me.currentMapPlan.get('isTemplate') == true || !me.currentMapPlan.get('id') || me.currentMapPlan.get('id') == ""
                        },
                        {
                            xtype: 'button',
                            text: me.textStore.getValueByCode('ssp.label.save-template-button','Save Template'),
                            itemId: 'saveTemplateButton',
                            hidden: (!me.currentMapPlan.get('id') || me.currentMapPlan.get('id') != "") || me.currentMapPlan.get('isTemplate') == false
                        },
                        {
                            xtype: 'button',
                            text: me.textStore.getValueByCode('ssp.label.save-as-new-plan-button','Save as New Plan'),
                            itemId: 'savePlanAsButton'
                        },
                        {
                            xtype: 'button',
                            text: me.textStore.getValueByCode('ssp.label.save-as-new-template-button','Save as New Template'),
                            itemId: 'saveTemplateAsButton'
                        }]
                    }
                }, {
                    tooltip: me.textStore.getValueByCode('ssp.tooltip.cancel-button','Cancel'),
                    text: '<u>' + me.textStore.getValueByCode('ssp.label.cancel-button','Cancel') + '</u>',
                    hidden: !me.authenticatedPerson.hasAccess('MAP_TOOL_LOAD_BUTTON'),
                    height: 22,
                    xtype: 'button',
                    itemId: 'cancelMapButton'
                },
                {
                    xtype: 'tbspacer',
                    flex: 0.05
                }]
            }, {
                xtype: 'plantool'
            }, {
                xtype: 'moveplan'
            }]
        });
        
        return me.callParent(arguments);
    }
    
});