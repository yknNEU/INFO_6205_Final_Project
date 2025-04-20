package com.phasmidsoftware.dsaipg.graphs.gis;

import com.phasmidsoftware.dsaipg.graphs.undirected.Edge;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeoEdgeTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAttribute() {
    }

    @Test
    public void get() {
    }

    @Test
    public void getOther() {
    }

    @Test
    public void equalsTest() {
        GeoPoint london = new MockGeoPoint("London", new Position_Spherical(51.5, 0));
        GeoPoint boston = new MockGeoPoint("Boston", new Position_Spherical(42.3, -71));
        Edge<GeoPoint, String> target1 = new GeoEdge<>(london, boston, "across the pond");
        Edge<GeoPoint, String> target2 = new GeoEdge<>(boston, london, "across the pond");
        assertEquals(target1, target2);
    }


    @Test
    public void hashCodeTest() {
    }

    @Test
    public void toStringTest() {
    }

    @Test
    public void create() {
    }
}