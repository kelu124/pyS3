package org.bytedeco.javacv;

import com.itextpdf.text.Element;
import java.lang.reflect.Array;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.IplImage;

public class Blobs {
    public static int BLOBAREA = 3;
    static int BLOBCOLCOUNT = 2700;
    public static int BLOBCOLOR = 2;
    public static int BLOBDATACOUNT = 14;
    public static int BLOBLABEL = 0;
    public static int BLOBMAXX = 11;
    public static int BLOBMAXY = 13;
    public static int BLOBMINX = 10;
    public static int BLOBMINY = 12;
    public static int BLOBPARENT = 1;
    public static int BLOBPERIMETER = 4;
    static int BLOBROWCOUNT = 3500;
    public static int BLOBSUMX = 5;
    public static int BLOBSUMXX = 7;
    public static int BLOBSUMXY = 9;
    public static int BLOBSUMY = 6;
    public static int BLOBSUMYY = 8;
    static int BLOBTOTALCOUNT = ((BLOBROWCOUNT + BLOBCOLCOUNT) * 5);
    public static int[] CondensationMap = new int[BLOBTOTALCOUNT];
    public static int[][] LabelMat = ((int[][]) Array.newInstance(Integer.TYPE, new int[]{BLOBROWCOUNT, BLOBCOLCOUNT}));
    public static int MaxLabel;
    public static double[][] RegionData = ((double[][]) Array.newInstance(Double.TYPE, new int[]{BLOBTOTALCOUNT, BLOBDATACOUNT}));
    public static int[] SubsumedLabel = new int[BLOBTOTALCOUNT];
    static double iField;
    static double[] iProperty;
    static double jField;
    static double[] jProperty;
    public int ColorA;
    public int ColorB;
    public int ColorC;
    public int ColorD;
    public int LabelA;
    public int LabelB;
    public int LabelC;
    public int LabelD;
    public int jcol;
    public int jrow;

    public void PrintRegionData() {
        PrintRegionData(0, MaxLabel);
    }

    public void PrintRegionData(int Label0, int Label1) {
        if (Label0 < 0) {
            Label0 = 0;
        }
        if (Label1 > MaxLabel) {
            Label1 = MaxLabel;
        }
        if (Label1 >= Label0) {
            for (int Label = Label0; Label <= Label1; Label++) {
                double[] Property = RegionData[Label];
                int ThisLabel = (int) Property[BLOBLABEL];
                int ThisParent = (int) Property[BLOBPARENT];
                int ThisColor = (int) Property[BLOBCOLOR];
                double ThisArea = Property[BLOBAREA];
                double ThisPerimeter = Property[BLOBPERIMETER];
                double ThisSumX = Property[BLOBSUMX];
                double ThisSumY = Property[BLOBSUMY];
                double ThisSumXX = Property[BLOBSUMXX];
                double ThisSumYY = Property[BLOBSUMYY];
                double ThisSumXY = Property[BLOBSUMXY];
                int ThisMinX = (int) Property[BLOBMINX];
                int ThisMaxX = (int) Property[BLOBMAXX];
                int ThisMinY = (int) Property[BLOBMINY];
                int ThisMaxY = (int) Property[BLOBMAXY];
                String Str1 = " " + Label + ": L[" + ThisLabel + "] P[" + ThisParent + "] C[" + ThisColor + "]";
                String Str2 = " AP[" + ThisArea + ", " + ThisPerimeter + "]";
                String Str3 = " M1[" + ThisSumX + ", " + ThisSumY + "] M2[" + ThisSumXX + ", " + ThisSumYY + ", " + ThisSumXY + "]";
                System.out.println(Str1 + Str2 + Str3 + (" MINMAX[" + ThisMinX + ", " + ThisMaxX + ", " + ThisMinY + ", " + ThisMaxY + "]"));
            }
            System.out.println();
        }
    }

    public static int NextRegion(int Parent, int Color, double MinArea, double MaxArea, int Label) {
        double DParent = (double) Parent;
        double DColor = (double) Color;
        if (DColor > 0.0d) {
            DColor = 1.0d;
        }
        int i = Label;
        while (i <= MaxLabel) {
            double[] Region = RegionData[i];
            double ThisParent = Region[BLOBPARENT];
            double ThisColor = Region[BLOBCOLOR];
            if ((DParent < 0.0d || DParent == ThisParent) && ((DColor < 0.0d || DColor == ThisColor) && Region[BLOBAREA] >= MinArea && Region[BLOBAREA] <= MaxArea)) {
                break;
            }
            i++;
        }
        if (i > MaxLabel) {
            return -1;
        }
        return i;
    }

    public static int PriorRegion(int Parent, int Color, double MinArea, double MaxArea, int Label) {
        double DParent = (double) Parent;
        double DColor = (double) Color;
        if (DColor > 0.0d) {
            DColor = 1.0d;
        }
        int i = Label;
        while (i >= 0) {
            double[] Region = RegionData[i];
            double ThisParent = Region[BLOBPARENT];
            double ThisColor = Region[BLOBCOLOR];
            if ((DParent < 0.0d || DParent == ThisParent) && ((DColor < 0.0d || DColor == ThisColor) && Region[BLOBAREA] >= MinArea && Region[BLOBAREA] <= MaxArea)) {
                break;
            }
            i--;
        }
        if (i < 0) {
            return -1;
        }
        return i;
    }

    public void ResetRegion(int Label) {
        double[] RegionD = RegionData[Label];
        int i = BLOBLABEL;
        int i2 = BLOBPARENT;
        int i3 = BLOBCOLOR;
        int i4 = BLOBAREA;
        int i5 = BLOBPERIMETER;
        int i6 = BLOBSUMX;
        int i7 = BLOBSUMY;
        int i8 = BLOBSUMXX;
        int i9 = BLOBSUMYY;
        int i10 = BLOBSUMXY;
        int i11 = BLOBMINX;
        int i12 = BLOBMAXX;
        int i13 = BLOBMINY;
        RegionD[BLOBMAXY] = 0.0d;
        RegionD[i13] = 0.0d;
        RegionD[i12] = 0.0d;
        RegionD[i11] = 0.0d;
        RegionD[i10] = 0.0d;
        RegionD[i9] = 0.0d;
        RegionD[i8] = 0.0d;
        RegionD[i7] = 0.0d;
        RegionD[i6] = 0.0d;
        RegionD[i5] = 0.0d;
        RegionD[i4] = 0.0d;
        RegionD[i3] = 0.0d;
        RegionD[i2] = 0.0d;
        RegionD[i] = 0.0d;
        System.arraycopy(RegionD, 0, RegionData[Label], 0, BLOBDATACOUNT);
    }

    public void OldRegion(int NewLabelD, int Label1, int Label2) {
        int i;
        int DeltaPerimeter = 0;
        if (Label1 >= 0 && Label1 != NewLabelD) {
            DeltaPerimeter = 0 + 1;
            double[] Region1 = RegionData[Label1];
            i = BLOBPERIMETER;
            Region1[i] = Region1[i] + 1.0d;
            System.arraycopy(Region1, 0, RegionData[Label1], 0, BLOBDATACOUNT);
        }
        if (Label2 >= 0 && Label2 != NewLabelD) {
            DeltaPerimeter++;
            double[] Region2 = RegionData[Label2];
            i = BLOBPERIMETER;
            Region2[i] = Region2[i] + 1.0d;
            System.arraycopy(Region2, 0, RegionData[Label2], 0, BLOBDATACOUNT);
        }
        this.LabelD = NewLabelD;
        double[] RegionD = RegionData[this.LabelD];
        RegionD[BLOBLABEL] = (double) this.LabelD;
        i = BLOBPARENT;
        RegionD[i] = RegionD[i] + 0.0d;
        i = BLOBCOLOR;
        RegionD[i] = RegionD[i] + 0.0d;
        i = BLOBAREA;
        RegionD[i] = RegionD[i] + 1.0d;
        i = BLOBPERIMETER;
        RegionD[i] = RegionD[i] + ((double) DeltaPerimeter);
        i = BLOBSUMX;
        RegionD[i] = RegionD[i] + ((double) this.jcol);
        i = BLOBSUMY;
        RegionD[i] = RegionD[i] + ((double) this.jrow);
        i = BLOBSUMXX;
        RegionD[i] = RegionD[i] + ((double) (this.jcol * this.jcol));
        i = BLOBSUMYY;
        RegionD[i] = RegionD[i] + ((double) (this.jrow * this.jrow));
        i = BLOBSUMXY;
        RegionD[i] = RegionD[i] + ((double) (this.jcol * this.jrow));
        RegionD[BLOBMINX] = Math.min(RegionD[BLOBMINX], (double) this.jcol);
        RegionD[BLOBMAXX] = Math.max(RegionD[BLOBMAXX], (double) this.jcol);
        RegionD[BLOBMINY] = Math.min(RegionD[BLOBMINY], (double) this.jrow);
        RegionD[BLOBMAXY] = Math.max(RegionD[BLOBMAXY], (double) this.jrow);
        System.arraycopy(RegionD, 0, RegionData[this.LabelD], 0, BLOBDATACOUNT);
    }

    public void NewRegion(int ParentLabel) {
        int i = MaxLabel + 1;
        MaxLabel = i;
        this.LabelD = i;
        double[] RegionD = RegionData[this.LabelD];
        RegionD[BLOBLABEL] = (double) this.LabelD;
        RegionD[BLOBPARENT] = (double) ParentLabel;
        RegionD[BLOBCOLOR] = (double) this.ColorD;
        RegionD[BLOBAREA] = 1.0d;
        RegionD[BLOBPERIMETER] = 2.0d;
        RegionD[BLOBSUMX] = (double) this.jcol;
        RegionD[BLOBSUMY] = (double) this.jrow;
        RegionD[BLOBSUMXX] = (double) (this.jcol * this.jcol);
        RegionD[BLOBSUMYY] = (double) (this.jrow * this.jrow);
        RegionD[BLOBSUMXY] = (double) (this.jcol * this.jrow);
        RegionD[BLOBMINX] = (double) this.jcol;
        RegionD[BLOBMAXX] = (double) this.jcol;
        RegionD[BLOBMINY] = (double) this.jrow;
        RegionD[BLOBMAXY] = (double) this.jrow;
        System.arraycopy(RegionD, 0, RegionData[this.LabelD], 0, BLOBDATACOUNT);
        SubsumedLabel[this.LabelD] = -1;
        double[] RegionB = RegionData[this.LabelB];
        i = BLOBPERIMETER;
        RegionB[i] = RegionB[i] + 1.0d;
        System.arraycopy(RegionB, 0, RegionData[this.LabelB], 0, BLOBDATACOUNT);
        double[] RegionC = RegionData[this.LabelC];
        i = BLOBPERIMETER;
        RegionC[i] = RegionC[i] + 1.0d;
        System.arraycopy(RegionC, 0, RegionData[this.LabelC], 0, BLOBDATACOUNT);
    }

    public void Subsume(int GoodLabel, int BadLabel, int PSign) {
        this.LabelD = GoodLabel;
        double[] GoodRegion = RegionData[GoodLabel];
        double[] BadRegion = RegionData[BadLabel];
        GoodRegion[BLOBLABEL] = GoodRegion[BLOBLABEL];
        GoodRegion[BLOBPARENT] = GoodRegion[BLOBPARENT];
        GoodRegion[BLOBCOLOR] = GoodRegion[BLOBCOLOR];
        int i = BLOBAREA;
        GoodRegion[i] = GoodRegion[i] + BadRegion[BLOBAREA];
        i = BLOBPERIMETER;
        GoodRegion[i] = GoodRegion[i] + (BadRegion[BLOBPERIMETER] * ((double) PSign));
        i = BLOBSUMX;
        GoodRegion[i] = GoodRegion[i] + BadRegion[BLOBSUMX];
        i = BLOBSUMY;
        GoodRegion[i] = GoodRegion[i] + BadRegion[BLOBSUMY];
        i = BLOBSUMXX;
        GoodRegion[i] = GoodRegion[i] + BadRegion[BLOBSUMXX];
        i = BLOBSUMYY;
        GoodRegion[i] = GoodRegion[i] + BadRegion[BLOBSUMYY];
        i = BLOBSUMXY;
        GoodRegion[i] = GoodRegion[i] + BadRegion[BLOBSUMXY];
        GoodRegion[BLOBMINX] = Math.min(GoodRegion[BLOBMINX], BadRegion[BLOBMINX]);
        GoodRegion[BLOBMAXX] = Math.max(GoodRegion[BLOBMAXX], BadRegion[BLOBMAXX]);
        GoodRegion[BLOBMINY] = Math.min(GoodRegion[BLOBMINY], BadRegion[BLOBMINY]);
        GoodRegion[BLOBMAXY] = Math.max(GoodRegion[BLOBMAXY], BadRegion[BLOBMAXY]);
        System.arraycopy(GoodRegion, 0, RegionData[GoodLabel], 0, BLOBDATACOUNT);
    }

    public static int SubsumptionChain(int x) {
        return SubsumptionChain(x, 0);
    }

    public static int SubsumptionChain(int x, int Print) {
        String Str = "";
        if (Print > 0) {
            Str = "Subsumption chain for " + x + ": ";
        }
        int Lastx = x;
        while (x > -1) {
            Lastx = x;
            if (Print > 0) {
                Str = Str + " " + x;
            }
            if (x == 0) {
                break;
            }
            x = SubsumedLabel[x];
        }
        if (Print > 0) {
            System.out.println(Str);
        }
        return Lastx;
    }

    public int BlobAnalysis(IplImage Src, int Col0, int Row0, int Cols, int Rows, int Border, int MinArea) {
        CvMat SrcMat = Src.asCvMat();
        int SrcCols = SrcMat.cols();
        int SrcRows = SrcMat.rows();
        if (Col0 < 0) {
            Col0 = 0;
        }
        if (Row0 < 0) {
            Row0 = 0;
        }
        if (Cols < 0) {
            Cols = SrcCols;
        }
        if (Rows < 0) {
            Rows = SrcRows;
        }
        if (Col0 + Cols > SrcCols) {
            Cols = SrcCols - Col0;
        }
        if (Row0 + Rows > SrcRows) {
            Rows = SrcRows - Row0;
        }
        if (Cols > BLOBCOLCOUNT || Rows > BLOBROWCOUNT) {
            System.out.println("Error in Class Blobs: Image too large: Edit Blobs.java");
            System.exit(Element.WRITABLE_DIRECT);
            return 0;
        }
        int Label;
        int OldLabel;
        int FillColor = 0;
        if (Border > 0) {
            FillColor = 1;
        }
        this.LabelD = 0;
        this.LabelC = 0;
        this.LabelB = 0;
        this.LabelA = 0;
        this.ColorD = FillColor;
        this.ColorC = FillColor;
        this.ColorB = FillColor;
        this.ColorA = FillColor;
        for (int k = 0; k < BLOBTOTALCOUNT; k++) {
            SubsumedLabel[k] = -1;
        }
        MaxLabel = 0;
        double[] BorderRegion = RegionData[0];
        BorderRegion[BLOBLABEL] = 0.0d;
        BorderRegion[BLOBPARENT] = -1.0d;
        BorderRegion[BLOBAREA] = (double) ((Rows + Cols) + 4);
        BorderRegion[BLOBCOLOR] = (double) FillColor;
        BorderRegion[BLOBSUMX] = ((0.5d * ((2.0d + ((double) Cols)) * (((double) Cols) - 1.0d))) - ((double) Rows)) - 1.0d;
        BorderRegion[BLOBSUMY] = ((0.5d * ((2.0d + ((double) Rows)) * (((double) Rows) - 1.0d))) - ((double) Cols)) - 1.0d;
        BorderRegion[BLOBMINX] = -1.0d;
        BorderRegion[BLOBMINY] = -1.0d;
        BorderRegion[BLOBMAXX] = ((double) Cols) + 1.0d;
        BorderRegion[BLOBMAXY] = ((double) Rows) + 1.0d;
        System.arraycopy(BorderRegion, 0, RegionData[0], 0, BLOBDATACOUNT);
        for (int irow = Row0; irow < Row0 + Rows; irow++) {
            this.jrow = irow - Row0;
            for (int icol = Col0; icol < Col0 + Cols; icol++) {
                int Case;
                this.jcol = icol - Col0;
                this.ColorC = FillColor;
                this.ColorB = FillColor;
                this.ColorA = FillColor;
                this.LabelD = 0;
                this.LabelC = 0;
                this.LabelB = 0;
                this.LabelA = 0;
                this.ColorD = (int) SrcMat.get(this.jrow, this.jcol);
                if (this.jrow == 0 || this.jcol == 0) {
                    if (this.jcol > 0) {
                        this.ColorC = (int) SrcMat.get(this.jrow, this.jcol - 1);
                        this.LabelC = LabelMat[this.jrow][this.jcol - 1];
                    }
                    if (this.jrow > 0) {
                        this.ColorB = (int) SrcMat.get(this.jrow - 1, this.jcol);
                        this.LabelB = LabelMat[this.jrow - 1][this.jcol];
                    }
                } else {
                    this.ColorA = (int) SrcMat.get(this.jrow - 1, this.jcol - 1);
                    if (this.ColorA > 0) {
                        this.ColorA = 1;
                    }
                    this.ColorB = (int) SrcMat.get(this.jrow - 1, this.jcol);
                    if (this.ColorB > 0) {
                        this.ColorB = 1;
                    }
                    this.ColorC = (int) SrcMat.get(this.jrow, this.jcol - 1);
                    if (this.ColorC > 0) {
                        this.ColorC = 1;
                    }
                    this.LabelA = LabelMat[this.jrow - 1][this.jcol - 1];
                    this.LabelB = LabelMat[this.jrow - 1][this.jcol];
                    this.LabelC = LabelMat[this.jrow][this.jcol - 1];
                }
                if (this.ColorA > 0) {
                    this.ColorA = 1;
                }
                if (this.ColorB > 0) {
                    this.ColorB = 1;
                }
                if (this.ColorC > 0) {
                    this.ColorC = 1;
                }
                if (this.ColorD > 0) {
                    this.ColorD = 1;
                }
                if (this.ColorA == this.ColorB) {
                    if (this.ColorC == this.ColorD) {
                        Case = this.ColorA == this.ColorC ? 1 : 2;
                    } else {
                        Case = this.ColorA == this.ColorC ? 5 : 6;
                    }
                } else {
                    if (this.ColorC == this.ColorD) {
                        Case = this.ColorA == this.ColorC ? 3 : 4;
                    } else {
                        Case = this.ColorA == this.ColorC ? 7 : 8;
                    }
                }
                if (Case == 1) {
                    OldRegion(this.LabelC, -1, -1);
                } else if (Case == 2 || Case == 3) {
                    OldRegion(this.LabelC, this.LabelB, this.LabelC);
                } else if (Case == 5 || Case == 8) {
                    if ((this.jrow == Rows || this.jcol == Cols) && this.ColorD == FillColor) {
                        OldRegion(0, -1, -1);
                    } else {
                        NewRegion(this.LabelB);
                    }
                } else if (Case == 6 || Case == 7) {
                    OldRegion(this.LabelB, this.LabelB, this.LabelC);
                } else {
                    int LabelX;
                    int LabelBRoot = SubsumptionChain(this.LabelB);
                    int LabelCRoot = SubsumptionChain(this.LabelC);
                    int LabelRoot = Math.min(LabelBRoot, LabelCRoot);
                    if (LabelBRoot < LabelCRoot) {
                        OldRegion(this.LabelB, -1, -1);
                        LabelX = this.LabelC;
                    } else {
                        OldRegion(this.LabelC, -1, -1);
                        LabelX = this.LabelB;
                    }
                    int NextLabelX = LabelX;
                    while (LabelRoot < LabelX) {
                        NextLabelX = SubsumedLabel[LabelX];
                        SubsumedLabel[LabelX] = LabelRoot;
                        LabelX = NextLabelX;
                    }
                }
                if ((this.jrow == Rows || this.jcol == Cols) && this.ColorD == FillColor) {
                    if (this.jcol < Cols) {
                        if (this.ColorC != FillColor) {
                            SubsumedLabel[SubsumptionChain(this.LabelB)] = 0;
                        }
                    } else if (this.jrow < Rows && this.ColorB != FillColor) {
                        SubsumedLabel[SubsumptionChain(this.LabelC)] = 0;
                    }
                    OldRegion(0, -1, -1);
                }
                LabelMat[this.jrow][this.jcol] = this.LabelD;
            }
        }
        int Offset = 0;
        for (Label = 1; Label <= MaxLabel; Label++) {
            if (SubsumedLabel[Label] > -1) {
                Offset++;
            }
            CondensationMap[Label] = Label - Offset;
        }
        for (Label = 1; Label <= MaxLabel; Label++) {
            int BetterLabel = SubsumptionChain(Label);
            if (BetterLabel != Label) {
                Subsume(BetterLabel, Label, 1);
            }
        }
        int NewMaxLabel = 0;
        for (OldLabel = 1; OldLabel <= MaxLabel; OldLabel++) {
            if (SubsumedLabel[OldLabel] < 0) {
                Object OldRegion = RegionData[OldLabel];
                int OldParent = (int) OldRegion[BLOBPARENT];
                int NewLabel = CondensationMap[OldLabel];
                int NewParent = CondensationMap[SubsumptionChain(OldParent)];
                OldRegion[BLOBLABEL] = (double) NewLabel;
                OldRegion[BLOBPARENT] = (double) NewParent;
                System.arraycopy(OldRegion, 0, RegionData[NewLabel], 0, BLOBDATACOUNT);
                NewMaxLabel = NewLabel;
            }
        }
        for (Label = NewMaxLabel + 1; Label <= MaxLabel; Label++) {
            ResetRegion(Label);
        }
        MaxLabel = NewMaxLabel;
        for (Label = MaxLabel; Label > 0; Label--) {
            double[] ThisRegion = RegionData[Label];
            if (((int) ThisRegion[BLOBAREA]) < MinArea) {
                SubsumedLabel[Label] = (int) ThisRegion[BLOBPARENT];
            } else {
                SubsumedLabel[Label] = -1;
            }
        }
        Offset = 0;
        for (Label = 1; Label <= MaxLabel; Label++) {
            if (SubsumedLabel[Label] > -1) {
                Offset++;
            }
            CondensationMap[Label] = Label - Offset;
        }
        for (Label = 1; Label <= MaxLabel; Label++) {
            BetterLabel = SubsumptionChain(Label);
            if (BetterLabel != Label) {
                Subsume(BetterLabel, Label, -1);
            }
        }
        for (OldLabel = 1; OldLabel <= MaxLabel; OldLabel++) {
            if (SubsumedLabel[OldLabel] < 0) {
                OldRegion = RegionData[OldLabel];
                OldParent = (int) OldRegion[BLOBPARENT];
                NewLabel = CondensationMap[OldLabel];
                NewParent = CondensationMap[SubsumptionChain(OldParent)];
                OldRegion[BLOBLABEL] = (double) NewLabel;
                OldRegion[BLOBPARENT] = (double) NewParent;
                System.arraycopy(OldRegion, 0, RegionData[NewLabel], 0, BLOBDATACOUNT);
                NewMaxLabel = NewLabel;
            }
        }
        for (Label = NewMaxLabel + 1; Label <= MaxLabel; Label++) {
            ResetRegion(Label);
        }
        MaxLabel = NewMaxLabel;
        for (Label = 0; Label <= MaxLabel; Label++) {
            Object ThisRegion2 = RegionData[Label];
            double Area = ThisRegion2[BLOBAREA];
            double SumX = ThisRegion2[BLOBSUMX] / Area;
            double SumY = ThisRegion2[BLOBSUMY] / Area;
            double SumXX = (ThisRegion2[BLOBSUMXX] / Area) - (SumX * SumX);
            double SumYY = (ThisRegion2[BLOBSUMYY] / Area) - (SumY * SumY);
            double SumXY = (ThisRegion2[BLOBSUMXY] / Area) - (SumX * SumY);
            if (SumXY > -1.0E-14d && SumXY < 1.0E-14d) {
                SumXY = 0.0d;
            }
            ThisRegion2[BLOBSUMX] = SumX;
            ThisRegion2[BLOBSUMY] = SumY;
            ThisRegion2[BLOBSUMXX] = SumXX;
            ThisRegion2[BLOBSUMYY] = SumYY;
            ThisRegion2[BLOBSUMXY] = SumXY;
            System.arraycopy(ThisRegion2, 0, RegionData[Label], 0, BLOBDATACOUNT);
        }
        BorderRegion = RegionData[0];
        int i = BLOBSUMXX;
        int i2 = BLOBSUMYY;
        BorderRegion[BLOBSUMXY] = 0.0d;
        BorderRegion[i2] = 0.0d;
        BorderRegion[i] = 0.0d;
        System.arraycopy(BorderRegion, 0, RegionData[0], 0, BLOBDATACOUNT);
        return MaxLabel;
    }

    public static void SortRegions(int Col) {
        for (int i = 0; i < MaxLabel; i++) {
            for (int j = i + 1; j <= MaxLabel; j++) {
                iProperty = RegionData[i];
                jProperty = RegionData[j];
                iField = iProperty[Col];
                jField = jProperty[Col];
                if (iField > jField) {
                    RegionData[i] = jProperty;
                    RegionData[j] = iProperty;
                }
            }
        }
    }
}
