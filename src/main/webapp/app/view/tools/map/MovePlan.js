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
Ext.define('Ssp.view.tools.map.MovePlan', {
    extend: 'Ext.form.Panel',
    alias: 'widget.moveplan',
    mixins: ['Deft.mixin.Injectable', 'Deft.mixin.Controllable'],
    controller: 'Ssp.controller.tool.map.MapPlanToolViewController',
    inject: {
        columnRendererUtils: 'columnRendererUtils',
        termsStore: 'termsStore',
        authenticatedPerson: 'authenticatedPerson',
    	currentMapPlan: 'currentMapPlan',
    	textStore: 'sspTextStore'
    },
    width: '100%',
    height: '38',
	style: 'background-color: lightgrey;',
    initComponent: function(){
        var me = this;
        Ext.apply(me, {
            fieldLabel: '',
            layout: 'hbox',
            margin: '0 0 0 0',
			padding: '0 0 0 0',
            defaultType: 'displayfield',
            bodyStyle: {"background-color":"lightgrey"},  
            fieldDefaults: {
                msgTarget: 'side'
            },
            items: [

                 {
                            fieldLabel: me.currentMapPlan.get("isTemplate") == true ? me.textStore.getValueByCode('ssp.label.map.move-plan.template-title','Template Title'):me.textStore.getValueByCode('ssp.label.map.move-plan.plan-=title','Plan Title'),
        					xtype:'displayfield',
                            itemId: 'name',
                            name: 'name',
        					frame: false,
        				    fieldStyle: 'text-align: left',
        					readOnly: true,
        					editable:false
                        }, 	
                        {
                            xtype: 'tbspacer',
                            flex: 0.5
                        },                        
        				{
        								 xtype: 'button',
        								 width: 20,
        								itemId:'onPlanStatusDetails',
        				    	         cls: 'helpIconSmall',
        				    	         tooltip: me.textStore.getValueByCode('ssp.tooltip.map.move-plan.on-plan','Student is currently on plan.')
        				    	     },{
                            		fieldLabel: me.textStore.getValueByCode('ssp.label.map.move-plan.on-plan','Plan Status'),
        		                    itemId: 'onPlan',
        		                    name: 'onPlan',
        							fieldStyle:"text-align:left"
        				   },
                           {
                               xtype: 'tbspacer',
                               flex: 0.5
                           },         				   
        		{
                    fieldLabel: me.textStore.getValueByCode('ssp.label.map.move-plan.current-total-plan-cr-hours','Plan Hrs'),
                    itemId: 'currentTotalPlanCrHrs',
                    name: 'currentTotalPlanCrHrs',
                    id: 'currentTotalPlanCrHrs',
				    fieldStyle: 'text-align: left'
                
                },                        {
                    xtype: 'tbspacer',
                    flex: 0.5
                },  {
                    fieldLabel: me.textStore.getValueByCode('ssp.label.map.move-plan.current-plan-total-dev-hours','Dev Hrs'),
                    itemId: 'currentPlanTotalDevCrHrs',
                    name: 'currentPlanTotalDevCrHrs',
                    id: 'currentPlanTotalDevCrHrs',
				    fieldStyle: 'text-align: left'
                
                },	{
		                    xtype: 'tbspacer',
		                    flex: 0.05
		                }]
        
        });
        
        return me.callParent(arguments);
    }
    
});
