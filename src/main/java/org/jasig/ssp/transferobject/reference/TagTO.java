/**
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
package org.jasig.ssp.transferobject.reference;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jasig.ssp.model.reference.Tag;
import org.jasig.ssp.transferobject.TransferObject;
import com.google.common.collect.Lists;


/**
 *Tag transfer object
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagTO
		extends AbstractReferenceTO<Tag>
		implements TransferObject<Tag> { 

	private String code;

	/**
	 * Empty constructor
	 */
	public TagTO() {
		super();
	}
	
	public TagTO(final Tag model) {
		super();
		from(model);
	}

	public TagTO(final UUID id, final String name,
			final String description) {
		super(id, name, description);
	}

	@Override
	public final void from(final Tag model) {
		/*if (model == null) {
			throw new IllegalArgumentException("Model can not be null.");
		}*/

		super.from(model);
		code = model.getCode();
		
	}

	public static List<TagTO> toTOList(
			final Collection<Tag> models) {
		final List<TagTO> tObjects = Lists.newArrayList();
		for (final Tag model : models) {
			tObjects.add(new TagTO(model)); 
		}

		return tObjects;
	}
	
	public String getCode(){
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
}