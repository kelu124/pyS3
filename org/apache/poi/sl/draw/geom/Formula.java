package org.apache.poi.sl.draw.geom;

import com.itextpdf.text.html.HtmlTags;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

public abstract class Formula {
    static Map<String, Formula> builtInFormulas = new HashMap();

    static class C10861 extends Formula {
        C10861() {
        }

        double evaluate(Context ctx) {
            return 1.62E7d;
        }
    }

    static class C10872 extends Formula {
        C10872() {
        }

        double evaluate(Context ctx) {
            return 8100000.0d;
        }
    }

    static class C10883 extends Formula {
        C10883() {
        }

        double evaluate(Context ctx) {
            return 1.62E7d;
        }
    }

    static class C10894 extends Formula {
        C10894() {
        }

        double evaluate(Context ctx) {
            return 1.62E7d;
        }
    }

    static class C10905 extends Formula {
        C10905() {
        }

        double evaluate(Context ctx) {
            Rectangle2D anchor = ctx.getShapeAnchor();
            return anchor.getY() + anchor.getHeight();
        }
    }

    static class C10916 extends Formula {
        C10916() {
        }

        double evaluate(Context ctx) {
            return 1.08E7d;
        }
    }

    static class C10927 extends Formula {
        C10927() {
        }

        double evaluate(Context ctx) {
            return 5400000.0d;
        }
    }

    static class C10938 extends Formula {
        C10938() {
        }

        double evaluate(Context ctx) {
            return 2700000.0d;
        }
    }

    static class C10949 extends Formula {
        C10949() {
        }

        double evaluate(Context ctx) {
            Rectangle2D anchor = ctx.getShapeAnchor();
            return anchor.getX() + (anchor.getWidth() / 2.0d);
        }
    }

    abstract double evaluate(Context context);

    String getName() {
        return null;
    }

    static {
        builtInFormulas.put("3cd4", new C10861());
        builtInFormulas.put("3cd8", new C10872());
        builtInFormulas.put("5cd8", new C10883());
        builtInFormulas.put("7cd8", new C10894());
        builtInFormulas.put(HtmlTags.f33B, new C10905());
        builtInFormulas.put("cd2", new C10916());
        builtInFormulas.put("cd4", new C10927());
        builtInFormulas.put("cd8", new C10938());
        builtInFormulas.put("hc", new C10949());
        builtInFormulas.put("h", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getHeight();
            }
        });
        builtInFormulas.put("hd2", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getHeight() / 2.0d;
            }
        });
        builtInFormulas.put("hd3", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getHeight() / 3.0d;
            }
        });
        builtInFormulas.put("hd4", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getHeight() / 4.0d;
            }
        });
        builtInFormulas.put("hd5", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getHeight() / 5.0d;
            }
        });
        builtInFormulas.put("hd6", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getHeight() / 6.0d;
            }
        });
        builtInFormulas.put("hd8", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getHeight() / 8.0d;
            }
        });
        builtInFormulas.put("l", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getX();
            }
        });
        builtInFormulas.put("ls", new Formula() {
            double evaluate(Context ctx) {
                Rectangle2D anchor = ctx.getShapeAnchor();
                return Math.max(anchor.getWidth(), anchor.getHeight());
            }
        });
        builtInFormulas.put("r", new Formula() {
            double evaluate(Context ctx) {
                Rectangle2D anchor = ctx.getShapeAnchor();
                return anchor.getX() + anchor.getWidth();
            }
        });
        builtInFormulas.put("ss", new Formula() {
            double evaluate(Context ctx) {
                Rectangle2D anchor = ctx.getShapeAnchor();
                return Math.min(anchor.getWidth(), anchor.getHeight());
            }
        });
        builtInFormulas.put("ssd2", new Formula() {
            double evaluate(Context ctx) {
                Rectangle2D anchor = ctx.getShapeAnchor();
                return Math.min(anchor.getWidth(), anchor.getHeight()) / 2.0d;
            }
        });
        builtInFormulas.put("ssd4", new Formula() {
            double evaluate(Context ctx) {
                Rectangle2D anchor = ctx.getShapeAnchor();
                return Math.min(anchor.getWidth(), anchor.getHeight()) / 4.0d;
            }
        });
        builtInFormulas.put("ssd6", new Formula() {
            double evaluate(Context ctx) {
                Rectangle2D anchor = ctx.getShapeAnchor();
                return Math.min(anchor.getWidth(), anchor.getHeight()) / 6.0d;
            }
        });
        builtInFormulas.put("ssd8", new Formula() {
            double evaluate(Context ctx) {
                Rectangle2D anchor = ctx.getShapeAnchor();
                return Math.min(anchor.getWidth(), anchor.getHeight()) / 8.0d;
            }
        });
        builtInFormulas.put("ssd16", new Formula() {
            double evaluate(Context ctx) {
                Rectangle2D anchor = ctx.getShapeAnchor();
                return Math.min(anchor.getWidth(), anchor.getHeight()) / 16.0d;
            }
        });
        builtInFormulas.put("ssd32", new Formula() {
            double evaluate(Context ctx) {
                Rectangle2D anchor = ctx.getShapeAnchor();
                return Math.min(anchor.getWidth(), anchor.getHeight()) / 32.0d;
            }
        });
        builtInFormulas.put("t", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getY();
            }
        });
        builtInFormulas.put("vc", new Formula() {
            double evaluate(Context ctx) {
                Rectangle2D anchor = ctx.getShapeAnchor();
                return anchor.getY() + (anchor.getHeight() / 2.0d);
            }
        });
        builtInFormulas.put("w", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getWidth();
            }
        });
        builtInFormulas.put("wd2", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getWidth() / 2.0d;
            }
        });
        builtInFormulas.put("wd3", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getWidth() / 3.0d;
            }
        });
        builtInFormulas.put("wd4", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getWidth() / 4.0d;
            }
        });
        builtInFormulas.put("wd5", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getWidth() / 5.0d;
            }
        });
        builtInFormulas.put("wd6", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getWidth() / 6.0d;
            }
        });
        builtInFormulas.put("wd8", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getWidth() / 8.0d;
            }
        });
        builtInFormulas.put("wd10", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getWidth() / 10.0d;
            }
        });
        builtInFormulas.put("wd32", new Formula() {
            double evaluate(Context ctx) {
                return ctx.getShapeAnchor().getWidth() / 32.0d;
            }
        });
    }
}
