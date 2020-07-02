package space.earlygrey.simplegraphs.utils;

public class VertexTypes {

    public interface SpatiallyEmbeddedVertex {

    }

    public class SpatiallyEmbeddedVertex2D implements SpatiallyEmbeddedVertex {
        protected float x, y;

        SpatiallyEmbeddedVertex2D() {

        }

        SpatiallyEmbeddedVertex2D(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float dst(SpatiallyEmbeddedVertex2D v) {
            final float x_d = v.x - x;
            final float y_d = v.y - y;
            return (float)Math.sqrt(x_d * x_d + y_d * y_d);
        }
    }

}
