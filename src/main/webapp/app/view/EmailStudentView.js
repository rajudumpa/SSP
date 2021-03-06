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
Ext.define('Ssp.view.EmailStudentView', {
    extend: 'Ext.window.Window',
    alias: 'widget.emailstudentview',
    height: 550,
    width: 750,
    resizable: true,
	modal: true,
	layout: 'fit',
    config: {
        isBulk: false,
        bulkCriteria: null
    },
    initComponent: function(){
        var me = this;

        if ( !(me.initialConfig.title) ) {
            me.title = me.getIsBulk() ? 'Email Students' : 'Email Student';
        }
        Ext.apply(me,{
            items: [
                {
                    xtype: 'emailstudentform',
                    isBulk: me.getIsBulk(),
                    bulkCriteria: me.getBulkCriteria()
                }
            ],
            listeners: {
                afterrender: function(c){
                    c.el.dom.setAttribute('role', 'dialog');
                }
            }
        });

    	me.callParent(arguments);
    }
});
