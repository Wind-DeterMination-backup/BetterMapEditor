package mindustry.maps.filters;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Scl;
import arc.util.Tmp;
import mindustry.graphics.Pal;
import mindustry.world.Tile;

import static mindustry.Vars.editor;

public class DraggableMirrorFilter extends MirrorFilter {
    private static final Vec2 dir = new Vec2();
    private static final Vec2 lineA = new Vec2();
    private static final Vec2 lineB = new Vec2();
    private static final Vec2 point = new Vec2();

    public float axisX = 0.5f;
    public float axisY = 0.5f;

    public static DraggableMirrorFilter fromMirror(MirrorFilter mirror) {
        DraggableMirrorFilter filter = new DraggableMirrorFilter();
        filter.seed = mirror.seed;
        filter.angle = mirror.angle;
        filter.rotate = mirror.rotate;
        return filter;
    }

    @Override
    public void apply(GenerateInput in) {
        float pivotX = axisCoord(axisX, in.width);
        float pivotY = axisCoord(axisY, in.height);

        dir.trnsExact(angle - 90f, 1f);
        lineA.set(pivotX + dir.x, pivotY + dir.y);
        lineB.set(pivotX - dir.x, pivotY - dir.y);
        point.set(in.x, in.y);

        if (!left(lineA, lineB, point)) {
            mirror(in.width, in.height, point, lineA.x, lineA.y, lineB.x, lineB.y);
            Tile tile = in.tile(point.x, point.y);
            in.floor = tile.floor();
            if (!tile.block().synthetic()) {
                in.block = tile.block();
            }
            in.overlay = tile.overlay();
            in.packedData = tile.getPackedData();
        }
    }

    @Override
    public void draw(Image image) {
        Rect drawRect = Tmp.r1;
        computePreviewRect(image, drawRect);
        if (drawRect.width <= 0f || drawRect.height <= 0f) return;

        float localPivotX = drawRect.x + axisXNorm() * drawRect.width;
        float localPivotY = drawRect.y + axisYNorm() * drawRect.height;

        Tmp.v1.trns(angle - 90f, 1f);
        clipHalfLine(Tmp.v1,
            drawRect.x - localPivotX,
            drawRect.y - localPivotY,
            drawRect.x + drawRect.width - localPivotX,
            drawRect.y + drawRect.height - localPivotY);

        Tmp.v2.set(Tmp.v1).scl(-1f);
        clipHalfLine(Tmp.v2,
            drawRect.x - localPivotX,
            drawRect.y - localPivotY,
            drawRect.x + drawRect.width - localPivotX,
            drawRect.y + drawRect.height - localPivotY);

        float px = image.x + localPivotX;
        float py = image.y + localPivotY;

        Tmp.v1.add(px, py);
        Tmp.v2.add(px, py);

        Lines.stroke(Scl.scl(3f), Pal.accent);
        Lines.line(Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y);

        Draw.color(Pal.accent);
        Fill.circle(px, py, Scl.scl(4f));
        Draw.color(Pal.darkestGray);
        Fill.circle(px, py, Scl.scl(1.8f));
        Draw.reset();
    }

    public void setAxisNormalized(float x, float y) {
        axisX = sanitizeNorm(x);
        axisY = sanitizeNorm(y);
    }

    public float axisXNorm() {
        return sanitizeNorm(axisX);
    }

    public float axisYNorm() {
        return sanitizeNorm(axisY);
    }

    public static void computePreviewRect(Image image, Rect out) {
        if (out == null) return;

        if (image == null || editor == null || editor.width() <= 0 || editor.height() <= 0) {
            out.set(0f, 0f, 0f, 0f);
            return;
        }

        float w = image.getWidth();
        float h = image.getHeight();

        if (w <= 0f || h <= 0f) {
            out.set(0f, 0f, 0f, 0f);
            return;
        }

        float mapW = editor.width();
        float mapH = editor.height();
        float scale = Math.min(w / mapW, h / mapH);

        float drawW = mapW * scale;
        float drawH = mapH * scale;
        out.set((w - drawW) * 0.5f, (h - drawH) * 0.5f, drawW, drawH);
    }

    private float axisCoord(float normalized, int size) {
        if (size <= 1) return 0f;
        return sanitizeNorm(normalized) * (size - 1f);
    }

    private static float sanitizeNorm(float value) {
        if (!Float.isFinite(value)) return 0.5f;
        return Mathf.clamp(value, 0f, 1f);
    }

}
