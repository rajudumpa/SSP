/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
Ext.define('Ssp.store.reference.Elective', {
    extend: 'Ssp.store.reference.AbstractReferences',
    model: 'Ssp.model.reference.Elective',
    constructor: function(){
    	this.callParent(arguments);
    	Ext.apply(this.getProxy(),{url: this.getProxy().url + this.apiProperties.getItemUrl('elective')});
    	this.load();
    },
	
	getActiveElectiveStore: function(){
		var store = Ext.create('Ext.data.Store', {
		     	model: "Ssp.model.reference.Elective"
		     });
    	store.loadData(this.queryBy(this.isActive).items);
		return store;
	},
	
	isActive: function(record, id){
    	var me = this;
    	if(record.get('objectStatus') == "ACTIVE")
    		return true;
    	return false;
    }
});
