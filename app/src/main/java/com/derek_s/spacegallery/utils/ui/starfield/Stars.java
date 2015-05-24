package com.derek_s.spacegallery.utils.ui.starfield;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Stars {
    private float size;
    private float speed;
    private int number;
    private final ArrayList<Pos> poss;
    private final ArrayList<Pos> old;
    private Random r = new Random();

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int RANDOM = 2;

    private int mWidth, mHeight;

    private static class Pos {
        private float x, y;
        private int mDirection;

        public Pos(float x, float y, int direction) {
            this.x = x;
            this.y = y;
            mDirection = direction;
        }

        public void updatePos(float move) {
            switch (mDirection) {
                case RIGHT:
                    x += move;
                    break;
                case LEFT:
                    x -= move;
                    break;
            }
        }
    }

    public Stars(float size, float speed, int number, int width, int height) {
        this.size = size;
        this.speed = speed;
        this.number = number;
        mWidth = width;
        mHeight = height;

        poss = new ArrayList<Stars.Pos>();
        old = new ArrayList<Stars.Pos>();
    }

    public void step(int direction) {
        if (r.nextInt(number) == 0) {
            int size = old.size();
            if (size > 0) {
                poss.add(old.remove(size - 1));
            } else {
                if (direction == RANDOM) {
                    direction = r.nextInt(RANDOM); // Random direction
                }
                poss.add(new Pos(-1, 0, direction));
            }
        }
        int width = mWidth;
        int height = mHeight;

        synchronized (poss) {
            Iterator<Pos> itr = poss.iterator();
            while (itr.hasNext()) {
                Pos pos = itr.next();

                switch (pos.mDirection) {
                    case RIGHT:
                        if (pos.x > width) {
                            pos.x = -1;
                            old.add(pos);
                            itr.remove();
                            continue;
                        }

                        if (pos.x < 0) {
                            pos.x = 0;
                            pos.y = r.nextInt() % height;
                        }
                        break;
                    case LEFT:
                        if (pos.x < 0) {
                            pos.x = mWidth + 1;
                            old.add(pos);
                            itr.remove();
                            continue;
                        }

                        if (pos.x > mWidth) {
                            pos.x = mWidth;
                            pos.y = r.nextInt() % height;
                        }
                        break;
                }
                pos.updatePos(speed);
            }
        }
    }

    public void draw(Canvas c, int width, int offset, Paint p) {
        for (Pos pos : poss) {
            if (pos.x + size > offset && pos.x < offset + width) {
                c.drawRect(pos.x - offset, pos.y, pos.x - offset + size, pos.y + size, p);
            }
        }
    }
}