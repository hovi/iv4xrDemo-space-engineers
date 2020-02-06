/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.Intersections;
import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.mesh.TriangleMesh;
import org.junit.Test;
import pathfinding.NavMeshContainer;
import world.Entity;
import world.InteractiveEntity;

import static org.junit.Assert.*;

public class EntityNodeIntersectionTest {
    //setup
    private TriangleMesh setup() {
        //this graph has 7 nodes 0 - 1 - 2 - 3
        //                               |
        //                               4 - 5 with a second layer with a single node on the top of node 2
        return new TriangleMesh(
                new NavMeshContainer(
                        new int[]{0,1,3,0,2,3,2,3,6,2,5,6,3,6,7,3,4,7,8,9,10},
                        new Vec3[]{
                                new Vec3(1, 0, 1),
                                new Vec3(2, 0, 1),
                                new Vec3(1, 0, 2),
                                new Vec3(2, 0, 2),
                                new Vec3(3, 0, 2),
                                new Vec3(1, 0, 3),
                                new Vec3(2, 0, 3),
                                new Vec3(3, 0, 3),
                                new Vec3(1, 3, 2),
                                new Vec3(2, 3, 2),
                                new Vec3(2, 3, 3)
                        }
                )
        );
    }

    @Test
    public void interSectionSingleNodeTest(){
        //create the triangle mesh
        TriangleMesh m = setup();

        //create the entity
        InteractiveEntity e = new InteractiveEntity();
        e.isActive = false;
        e.center = new Vec3(2,0.5,1);
        e.extents = new Vec3( 0.2, 0.5, 0.2);
        e.tag = "Door";

        //get the intersecting nodes
        Integer[] blockedNodes = EntityNodeIntersection.getNodesBlockedByInteractiveEntity(e,m);

        //check if only node 0 is blocked
        assertEquals(1, blockedNodes.length);
        assertEquals(0, blockedNodes[0].intValue());
    }

    @Test
    public void noInterSectionNodeTest(){
        //create the triangle mesh
        TriangleMesh m = setup();

        //create the entity
        InteractiveEntity e = new InteractiveEntity();
        e.isActive = false;
        e.center = new Vec3(5,0.5,5);
        e.extents = new Vec3( 0.2, 0.5, 0.2);
        e.tag = "Door";

        //get the intersecting nodes
        Integer[] blockedNodes = EntityNodeIntersection.getNodesBlockedByInteractiveEntity(e,m);

        //check if no nodes are blocked
        assertEquals(0, blockedNodes.length);
    }

    @Test
    public void interSectionDoubleNodeTest(){
        //create the triangle mesh
        TriangleMesh m = setup();

        //create the entity
        InteractiveEntity e = new InteractiveEntity();
        e.isActive = false;
        e.center = new Vec3(3,0.5,3);
        e.extents = new Vec3( 0.2, 0.5, 0.2);
        e.tag = "Door";

        //get the intersecting nodes
        Integer[] blockedNodes = EntityNodeIntersection.getNodesBlockedByInteractiveEntity(e,m);

        //check if nodes 4 and 5 are blocked
        assertEquals(2, blockedNodes.length);
        assertEquals(4, blockedNodes[0].intValue());
        assertEquals(5, blockedNodes[1].intValue());
    }

    @Test
    public void interSectionDifferentLayerTest(){
        //create the triangle mesh
        TriangleMesh m = setup();

        //create the entity
        InteractiveEntity e = new InteractiveEntity();
        e.isActive = false;
        e.center = new Vec3(2,3.5,2);
        e.extents = new Vec3( 0.2, 0.5, 0.2);
        e.tag = "Door";

        //get the intersecting nodes
        Integer[] blockedNodes = EntityNodeIntersection.getNodesBlockedByInteractiveEntity(e,m);

        //check if only node 6 is blocked and node 3 is not blocked
        assertEquals(1, blockedNodes.length);
        assertEquals(6, blockedNodes[0].intValue());
    }
}