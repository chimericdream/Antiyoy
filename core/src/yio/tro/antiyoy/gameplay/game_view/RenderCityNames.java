package yio.tro.antiyoy.gameplay.game_view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.antiyoy.gameplay.Hex;
import yio.tro.antiyoy.gameplay.Province;
import yio.tro.antiyoy.stuff.Fonts;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.PointYio;
import yio.tro.antiyoy.stuff.RectangleYio;

public class RenderCityNames extends GameRender{


    private float hvSize;
    private TextureRegion greenPixel;
    private TextureRegion blackTriangle;
    private Hex capitalHex;
    private PointYio hexPos;
    private float f;
    private float pWidth;
    private Color c;
    private Province province;
    public RectangleYio pos;
    RectangleYio trianglePos;
    private float trpSize;
    PointYio textPosition;
    private TextureRegion editIcon;
    RectangleYio editIconPosition;
    private float eiSize;


    public RenderCityNames(GrManager grManager) {
        super(grManager);

        pos = new RectangleYio();
        trianglePos = new RectangleYio();
        textPosition = new PointYio();
        editIconPosition = new RectangleYio();
    }


    @Override
    public void loadTextures() {
        greenPixel = loadTextureRegion("pixels/pixel_green.png", false);
        blackTriangle = loadTextureRegion("triangle.png", false);
        editIcon = loadTextureRegion("game/edit_icon.png", true);
    }


    @Override
    public void render() {
        if (!gameController.areCityNamesEnabled()) return;
        if (!gameController.isPlayerTurn()) return;

        hvSize = gameView.hexViewSize;

        for (Province province : gameController.fieldController.provinces) {
            if (gameController.isCurrentTurn(province.getColor()) && province.isSelected()) {
                renderSingleCityName(province);
            }
        }
    }


    private void renderSingleCityName(Province province) {
        prepare(province);
        updatePos();
        updateTrianglePos();
        updateTextPosition();
        updateEditIconPosition();

        batchMovable.setColor(c.r, c.g, c.b, f);
        renderBorder();
        renderBlackTriangle();

        batchMovable.setColor(c.r, c.g, c.b, 0.3f + 0.7f * f);
        renderInternalBackground();
        renderText();
        renderEditicon();

        batchMovable.setColor(c.r, c.g, c.b, c.a);
    }


    private void renderEditicon() {
        GraphicsYio.drawByRectangle(batchMovable, editIcon, editIconPosition);
    }


    private void updateEditIconPosition() {
        eiSize = (float) pos.height;
        editIconPosition.set(
                pos.x + pos.width - eiSize,
                pos.y,
                eiSize,
                eiSize
        );
    }


    private void updateTextPosition() {
        textPosition.set(
                pos.x + 0.1f * hvSize,
                pos.y + 0.7f * hvSize
        );
    }


    private void updateTrianglePos() {
        trpSize = 0.6f * hvSize;

        trianglePos.set(
                pos.x + pos.width / 2 - trpSize / 2,
                pos.y - 0.47f * hvSize - 0.1f * f * hvSize,
                trpSize,
                trpSize
        );
    }


    private void updatePos() {
        pos.set(
                hexPos.x - pWidth,
                hexPos.y + 0.7f * hvSize + 0.3f * f * hvSize,
                2 * pWidth,
                0.9f * hvSize
        );

        double changeDelta = 1.2f * pos.height;
        pos.width += changeDelta; // place for edit icon
        pos.x -= changeDelta / 2; // to keep triangle centered on city
    }


    private void renderText() {
        Fonts.microFont.draw(
                batchMovable,
                province.getName(),
                textPosition.x,
                textPosition.y
        );
    }


    private void renderBorder() {
        GraphicsYio.renderBorder(batchMovable, greenPixel, pos, 0.1f * hvSize);
    }


    private void renderInternalBackground() {
        GraphicsYio.drawByRectangle(batchMovable, gameView.blackPixel, pos);
    }


    private void renderBlackTriangle() {
        GraphicsYio.drawByRectangle(batchMovable, blackTriangle, trianglePos);
    }


    private void prepare(Province province) {
        this.province = province;
        capitalHex = province.getCapital();
        hexPos = capitalHex.getPos();
        f = capitalHex.selectionFactor.get() - gameController.fieldController.moveZoneManager.appearFactor.get();
        pWidth = province.nameWidth;
        c = batchMovable.getColor();
    }


    @Override
    public void disposeTextures() {
        greenPixel.getTexture().dispose();
        blackTriangle.getTexture().dispose();
        editIcon.getTexture().dispose();
    }
}
