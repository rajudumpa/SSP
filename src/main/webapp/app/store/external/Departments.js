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
Ext.define('Ssp.store.external.Departments', {
	extend: 'Ssp.store.reference.AbstractReferences',
	model: 'Ssp.model.external.AbstractExternal',
    mixins: [ 'Deft.mixin.Injectable' ],
    inject: {
    	apiProperties: 'apiProperties'
    },
   
    sortOnLoad: true,
    remoteSort: false,
    
    constructor: function(){
		var me = this;
		
		me.callParent(arguments);
		this.addListener('load', this.sortAfterLoad, me, {single:true});
    	Ext.apply(this.getProxy(),{url: this.getProxy().url + this.apiProperties.getItemUrl('department'),
    		autoLoad: true});
    	return; 
    },
	sortAfterLoad: function(){
		this.sort('name','ASC');
	}
});