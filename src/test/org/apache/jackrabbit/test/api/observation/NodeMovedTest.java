/*
 * Copyright 2004-2005 The Apache Software Foundation or its licensors,
 *                     as applicable.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.test.api.observation;

import javax.jcr.RepositoryException;
import javax.jcr.Node;
import javax.jcr.observation.Event;

/**
 * Tests if {@link javax.jcr.Session#move} operations trigger the appropriate
 * observation events.
 * <p/>
 * Configuration requirements are:<br/>
 * The {@link #testRoot} must allow child nodes of type {@link #testNodeType}.
 * The child nodes that are created will be named {@link #nodeName1},
 * {@link #nodeName2}, {@link #nodeName3} and {@link #nodeName4}. Furthermore
 * {@link #testNodeType} must allow to add child nodes of the same type
 * ({@link #testNodeType}).
 *
 * @test
 * @sources NodeMovedTest.java
 * @executeClass org.apache.jackrabbit.test.api.observation.NodeMovedTest
 * @keywords observation
 */
public class NodeMovedTest extends AbstractObservationTest {

    /**
     * Tests if node removed and node added event is triggered when a tree
     * is moved.
     */
    public void testMoveTree() throws RepositoryException {
        /**
         * Initial tree:
         *  + testroot
         *      + nodename1
         *          + nodename2
         *
         * After move:
         *  + testroot
         *      + nodename3
         *          + nodename2
         */

        Node n1 = testRootNode.addNode(nodeName1, testNodeType);
        n1.addNode(nodeName2, testNodeType);
        testRootNode.save();
        EventResult addNodeListener = new EventResult(log);
        EventResult removeNodeListener = new EventResult(log);
        addEventListener(addNodeListener, Event.NODE_ADDED);
        addEventListener(removeNodeListener, Event.NODE_REMOVED);
        superuser.move(n1.getPath(), testRoot + "/" + nodeName3);
        testRootNode.save();
        removeEventListener(addNodeListener);
        removeEventListener(removeNodeListener);
        Event[] added = addNodeListener.getEvents(DEFAULT_WAIT_TIMEOUT);
        Event[] removed = removeNodeListener.getEvents(DEFAULT_WAIT_TIMEOUT);
        checkNodeAdded(added, new String[]{nodeName3});
        checkNodeRemoved(removed, new String[]{nodeName1});
    }

    /**
     * Tests if node removed and node added event is triggered when a node
     * is moved.
     */
    public void testMoveNode() throws RepositoryException {
        /**
         * Initial tree:
         *  + testroot
         *      + nodename1
         *          + nodename2
         *
         * After move:
         *  + testroot
         *      + nodename1
         *      + nodename2
         */

        Node n1 = testRootNode.addNode(nodeName1, testNodeType);
        Node n2 = n1.addNode(nodeName2, testNodeType);
        testRootNode.save();
        EventResult addNodeListener = new EventResult(log);
        EventResult removeNodeListener = new EventResult(log);
        addEventListener(addNodeListener, Event.NODE_ADDED);
        addEventListener(removeNodeListener, Event.NODE_REMOVED);
        superuser.move(n2.getPath(), testRoot + "/" + nodeName2);
        testRootNode.save();
        removeEventListener(addNodeListener);
        removeEventListener(removeNodeListener);
        Event[] added = addNodeListener.getEvents(DEFAULT_WAIT_TIMEOUT);
        Event[] removed = removeNodeListener.getEvents(DEFAULT_WAIT_TIMEOUT);
        checkNodeAdded(added, new String[]{nodeName2});
        checkNodeRemoved(removed, new String[]{nodeName1 + "/" + nodeName2});
    }
}
