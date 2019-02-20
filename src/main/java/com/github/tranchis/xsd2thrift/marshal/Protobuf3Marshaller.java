/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * XSD2Thrift
 *
 * Copyright (C) 2009 Sergio Alvarez-Napagao http://www.sergio-alvarez.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
package com.github.tranchis.xsd2thrift.marshal;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class Protobuf3Marshaller implements IMarshaller {
    private TreeMap<String, String> typeMapping;
    private String indent = "";

    public Protobuf3Marshaller() {
        typeMapping = new TreeMap<String, String>();
        typeMapping.put("positiveInteger", "int64");
        typeMapping.put("nonPositiveInteger", "sint64");
        typeMapping.put("negativeInteger", "sint64");
        typeMapping.put("nonNegativeInteger", "int64");
        typeMapping.put("int", "int32");
        typeMapping.put("integer", "int64");

        typeMapping.put("unsignedLong", "uint64");
        typeMapping.put("unsignedInt", "uint32");
        typeMapping.put("unsignedShort", "uint32"); // No 16-bit int in protobuf
        typeMapping.put("unsignedByte", "uint32"); // No 8-bit int in protobuf

        typeMapping.put("short", "int32"); // No 16-bit int in protobuf
        typeMapping.put("long", "int64");
        typeMapping.put("decimal", "double");
        typeMapping.put("ID", "string");
        typeMapping.put("IDREF", "string");
        typeMapping.put("NMTOKEN", "string");
        typeMapping.put("NMTOKENS", "string"); // TODO: Fix this
        typeMapping.put("anySimpleType", "UnspecifiedType");
        typeMapping.put("anyType", "UnspecifiedType");
        typeMapping.put("anyURI", "UnspecifiedType");
        typeMapping.put("boolean", "bool");
        typeMapping.put("binary", "bytes"); // UnspecifiedType.object is
        // declared binary
        typeMapping.put("hexBinary", "bytes");
        typeMapping.put("base64Binary", "bytes");

        typeMapping.put("byte", "bytes");
        //typeMapping.put("date", "int32"); // Number of days since January 1st,
        typeMapping.put("date", "google.protobuf.Timestamp"); // Number of days since January 1st,
        // 1970
        // typeMapping.put("dateTime", "int64"); // Number of milliseconds since
        typeMapping.put("dateTime", "google.protobuf.Timestamp"); // Number of milliseconds since
        // January 1st, 1970
    }

    @Override
    public String writeHeader(String namespace, String name) {
        StringBuilder res = new StringBuilder("syntax = \"proto3\";\n\n");

        res.append("import \"google/protobuf/timestamp.proto\";\n\n");

        if (namespace != null && !namespace.isEmpty()) {
            res.append("package ").append(namespace).append(";\n\n");
        }

        res.append("option java_package = \"").append(namespace).append("\";\n");
        if (name != null && !"".equals(name)) {
            String classname = name.substring(0, name.lastIndexOf('.'));
            res.append("option java_outer_classname = \"").append(classname).append("\";\n");
        }
        res.append("option java_multiple_files = true;\n");
        res.append("option optimize_for = SPEED;\n\n");

        return res.toString();
    }

    @Override
    public String writeEnumHeader(String name) {
        final String result = writeIndent() + "enum " + capitalize(name) + "\n"
                + writeIndent() + "{\n";
        increaseIndent();
        return result;
    }

    @Override
    public String writeEnumValue(int order, String value) {
        return (writeIndent() + value + " = " + order + ";\n");
    }

    @Override
    public String writeEnumFooter() {
        decreaseIndent();
        return writeIndent() + "}\n";
    }

    @Override
    public String writeStructHeader(String name) {
        final String result = writeIndent() + "message " + capitalize(name) + "\n{\n";
        increaseIndent();
        return result;
    }

    @Override
    public String writeStructParameter(int order, boolean required,
                                       boolean repeated, String name, String type) {
        String sRequired;

        sRequired = getRequiredRepeated(required, repeated);

        Set<String> types = new HashSet<String>(typeMapping.values());
        String paramType;
        if (types.contains(type)) {
            paramType = type;
        } else {
            paramType = capitalize(type);
        }


        return writeIndent() + sRequired + " " + paramType + " " + name + " = "
                + order + ";\n";
    }

    private String getRequiredRepeated(boolean required, boolean repeated) {
        String res;

        if (repeated) {
            res = " repeated";
        } else {
            res = "";
        }

        return res;
    }

    @Override
    public String writeStructFooter() {
        decreaseIndent();
        return writeIndent() + "}\n\n";
    }

    @Override
    public String getTypeMapping(String type) {
        return typeMapping.get(type);
    }

    @Override
    public boolean isNestedEnums() {
        return true;
    }

    @Override
    public boolean isCircularDependencySupported() {
        return true;
    }

    private void increaseIndent() {
        indent += "\t";
    }

    private void decreaseIndent() {
        indent = indent.substring(0, indent.length() > 0 ? indent.length() - 1
                : 0);
    }

    private String writeIndent() {
        return indent;
    }

    @Override
    public String writeInclude(String namespace) {
        String res;

        if (namespace != null && !namespace.isEmpty()) {
            res = "import \"" + namespace + ".proto\";\n";
        } else {
            res = "";
        }

        return res;
    }

    @Override
    public void setCustomMappings(Map<String, String> customMappings) {
        if (customMappings != null) {
            for (Entry<String, String> entry : customMappings.entrySet()) {
                typeMapping.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public String getName() {
        return "proto3";
    }
}
