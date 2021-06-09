package com.mygdx.tns;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


public class TMObjectsUtils {
    public static Array<Fixture> buildBuildingsBodies(TiledMap tiledMap, World world, float density, String layerName) {
        MapObjects objects = tiledMap.getLayers().get(layerName).getObjects();
        Array<Fixture> fixtures = new Array<>();
        for (MapObject object : objects) {
            Shape shape;
            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject) object, density, layerName);
            } else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject) object, density);
            } else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject) object, density);
            } else if (object instanceof CircleMapObject) {
                shape = getCircle((CircleMapObject) object, density);
            } else {
                continue;
            }
            createBody(shape, world, layerName);
        }
        return fixtures;
    }

    private static Fixture createBody(Shape shape, World world, String layerName) {
        Fixture fixture;
        Body body;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);
        fixture = body.createFixture(shape, 1);
        shape.dispose();
        fixture.setUserData(layerName);
        fixture.getBody().setTransform(0, 0, 0);
        Filter filter = new Filter();
        fixture.setFriction(0);
        if (layerName == "EnemiesBarrier"){
            fixture.setSensor(true);
        }
        return fixture;
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject, float density, String layername) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width / 2) * density,
                (rectangle.y + rectangle.height / 2) * density);
        polygon.setAsBox(rectangle.width / 2 * density,
                rectangle.height / 2 * density,
                size,
                0.0f);
        if (layername == "Point") {
            Const.points.add(new Vector2(rectangle.x * Const.TiledMap_Scale, rectangle.y * Const.TiledMap_Scale));
        }
        return polygon;
    }

    private static CircleShape getCircle(CircleMapObject circleObject, float density) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius * density);
        circleShape.setPosition(new Vector2(circle.x * density, circle.y * density));
        return circleShape;
    }

    //    private static PolygonShape getPolygon(PolygonMapObject polygonObject, float density) {
    private static ChainShape getPolygon(PolygonMapObject polygonObject, float density) {
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();
        int length = vertices.length;
        Vector2[] worldVertices;
        if (vertices[0] == vertices[length - 2] && vertices[1] == vertices[length - 1]) {
            worldVertices = new Vector2[length / 2];
            // + 1 for last line
            for (int i = 0; i < length / 2; i++) {
                worldVertices[i] = new Vector2();
                worldVertices[i].x = vertices[i * 2] * density;
                worldVertices[i].y = vertices[i * 2 + 1] * density;
            }
        } else {
            worldVertices = new Vector2[length / 2 + 1];
            // + 1 for last line
            for (int i = 0; i < length / 2; i++) {
                worldVertices[i] = new Vector2();
                worldVertices[i].x = vertices[i * 2] * density;
                worldVertices[i].y = vertices[i * 2 + 1] * density;
            }
            worldVertices[worldVertices.length - 1] = new Vector2(vertices[0] * density, vertices[1] * density);
        }

//        PolygonShape polygon = new PolygonShape();
//        polygon.set(worldVertices);

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject, float density) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; i++) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] * density;
            worldVertices[i].y = vertices[i * 2 + 1] * density;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }
}
