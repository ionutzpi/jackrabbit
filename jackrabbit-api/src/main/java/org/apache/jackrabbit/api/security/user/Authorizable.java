/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.api.security.user;

import java.security.Principal;
import java.util.Iterator;

import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;

/**
 * The Authorizable is the common base interface for {@link User} and
 * {@link Group}. It provides access to the <code>Principal</code>s associated
 * with an <code>Authorizable</code> (see below) and allow to access and
 * modify additional properties such as e.g. full name, e-mail or address.
 * <p>
 * <p>
 * Please note the difference between <code>Authorizable</code> and
 * {@link java.security.Principal Principal}:<br>
 * An <code>Authorizable</code> is repository object that is neither associated
 * with nor depending from a particular <code>Session</code> and thus independent
 * of the login mechanisms creating <code>Session</code>s.<br>
 * <p>
 * On the other hand <code>Principal</code>s are representations of user
 * identities. In other words: each <code>Principal</code> within the set
 * associated with the Session's Subject upon login represents an identity for
 * that user. An the set of <code>Principal</code>s may differ between different
 * login mechanisms.<br>
 * <p>
 * Consequently an one-to-many relationship exists between Authorizable
 * and Principal (see also {@link #getPrincipal()}.
 * <p>
 * <p>
 * The interfaces derived from Authorizable are defined as follows:
 * <ul>
 * <li>{@link User}: defined to be an Authorizable that can be authenticated
 * (by using Credentials) and impersonated.</li>
 * <li>{@link Group}: defined to be a collection of other
 * <code>Authorizable</code>s.</li>
 * </ul>
 *
 * @see User
 * @see Group
 */
public interface Authorizable {

    /**
     * Return the implementation specific identifier for this
     * <code>Authorizable</code>. It could e.g. be a UserID or simply the
     * principal name.
     *
     * @return Name of this <code>Authorizable</code>.
     * @throws RepositoryException if an error occurs.
     */
    String getID() throws RepositoryException;

    /**
     * @return if the current Authorizable is a {@link Group}
     */
    boolean isGroup();

    /**
     * @return a representation as Principal.
     * @throws RepositoryException If an error occurs.
     */
    Principal getPrincipal() throws RepositoryException;

    /**
     * @return all {@link Group}s, this Authorizable is declared member of.
     * @throws RepositoryException If an error occurs.
     */
    Iterator<Group> declaredMemberOf() throws RepositoryException;

    /**
     * @return all {@link Group}s, this Authorizable is member of included
     *         indirect group membership.
     * @throws RepositoryException If an error occurs.
     */
    Iterator<Group> memberOf() throws RepositoryException;

    /**
     * Removes this <code>Authorizable</code>, if the session has sufficient
     * permissions. Note, that removing an <code>Authorizable</code> even
     * if it listed as member of a Group or if still has members (this is
     * a Group itself).
     *
     * @throws RepositoryException If an error occurred and the
     * <code>Authorizable</code> could not be removed.
     */
    void remove() throws RepositoryException;

    /**
     * Returns the names of properties present with <code>this</code>
     * Authorizable not taking possible relative paths into consideration.
     * Same as {@link #getPropertyNames(String)} where the specified string
     * is &quot;.&quot;.
     *
     * @return names of properties.
     * @throws RepositoryException If an error occurs.
     * @see #getProperty(String) where the specified relative path is simply an
     * name.
     * @see #hasProperty(String)
     */
    Iterator<String> getPropertyNames() throws RepositoryException;

    /**
     * Returns the names of properties present with <code>this</code>
     * Authorizable at the specified relative path.
     *
     * @param relPath A relative path.
     * @return names of properties.
     * @throws RepositoryException If an error occurs.
     * @see #getProperty(String)
     * @see #hasProperty(String)
     */
    Iterator<String> getPropertyNames(String relPath) throws RepositoryException;

    /**
     * Tests if a the property with specified name exists.
     *
     * @param relPath The relative path to the property to be tested.
     * @return <code>true</code> if a property with the given name exists.
     * @throws RepositoryException If an error occurs.
     * @see #getProperty(String)
     */
    boolean hasProperty(String relPath) throws RepositoryException;

    /**
     * Set an arbitrary property to this <code>Authorizable</code>.
     *
     * @param relPath The relative path of the property to be added or modified.
     * @param value The desired value.
     * @throws RepositoryException If the specified property could not be set.
     */
    void setProperty(String relPath, Value value) throws RepositoryException;

    /**
     * Set an arbitrary property to this <code>Authorizable</code>.
     *
     * @param relPath The relative path of the property to be added or modified.
     * @param value The desired property values.
     * @throws RepositoryException If the specified property could not be set.
     */
    void setProperty(String relPath, Value[] value) throws RepositoryException;

    /**
     * Returns the values for the properties with the specified name or
     * <code>null</code>.
     *
     * @param relPath Relative path of the property to be retrieved.
     * @return value of the property with the given name or <code>null</code>
     *         if no such property exists.
     * @throws RepositoryException If an error occurs.
     */
    Value[] getProperty(String relPath) throws RepositoryException;

    /**
     * Removes the property with the given name.
     *
     * @param relPath Relative path (or name) of the property to be removed.
     * @return true If the property at the specified relPath was successfully
     *         removed; false if no such property was present.
     * @throws RepositoryException If an error occurs.
     */
    boolean removeProperty(String relPath) throws RepositoryException;

    /**
     * Returns a JCR path if this authorizable instance is associated with an
     * item that can be accessed by the editing <code>Session</code>.<p/>
     * Throws <code>UnsupportedRepositoryOperationException</code> if this
     * method is not supported or if there is no item associated with this
     * authorizable that is accessible by the editing <code>Session</code>.<p/>
     * Throws <code>RepositoryException</code> if another error occurs while
     * retrieving the path.
     *
     * @return the path of the {@link javax.jcr.Item} that corresponds to this
     * <code>Authorizable</code>.
     * @throws UnsupportedRepositoryOperationException If this method is not
     * supported or if there exists no accessible item associated with this
     * <code>Authorizable</code> instance.
     * @throws RepositoryException If an error occurs while retrieving the
     * <code>Item</code> path.
     */
    String getPath() throws UnsupportedRepositoryOperationException, RepositoryException;
}
