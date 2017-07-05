/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cdancy.bitbucket.rest.domain.comment;

import com.cdancy.bitbucket.rest.domain.pullrequest.Author;
import com.cdancy.bitbucket.rest.utils.Utils;
import com.google.auto.value.AutoValue;
import java.util.Map;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

@AutoValue
public abstract class TaskAnchor {

    public abstract Map<String, String> properties();

    public abstract int id();

    public abstract int version();
        
    @Nullable
    public abstract String text();
    
    public abstract Author author();

    public abstract long createdDate();

    public abstract long updatedDate();
    
    public abstract PermittedOperations permittedOperations();

    public abstract String type();

    TaskAnchor() {
    }

    @SerializedNames({ "properties", "id", "version", "text", 
            "author", "createdDate", "updatedDate", "permittedOperations", "type" })
    public static TaskAnchor create(Map<String, String> properties, int id, int version, String text, 
            Author author, long createdDate, long updatedDate, 
            PermittedOperations permittedOperations, String type) {
        return new AutoValue_TaskAnchor(Utils.nullToEmpty(properties), id, version, text, 
                author, createdDate, updatedDate, permittedOperations, type);
    }
}
