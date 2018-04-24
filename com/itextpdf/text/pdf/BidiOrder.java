package com.itextpdf.text.pdf;

import com.google.common.base.Ascii;
import com.itextpdf.text.DocWriter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.languages.DevanagariLigaturizer;
import com.itextpdf.text.pdf.languages.GujaratiLigaturizer;

public final class BidiOrder {
    public static final byte AL = (byte) 4;
    public static final byte AN = (byte) 11;
    public static final byte f108B = (byte) 15;
    public static final byte BN = (byte) 14;
    public static final byte CS = (byte) 12;
    public static final byte EN = (byte) 8;
    public static final byte ES = (byte) 9;
    public static final byte ET = (byte) 10;
    public static final byte f109L = (byte) 0;
    public static final byte LRE = (byte) 1;
    public static final byte LRO = (byte) 2;
    public static final byte NSM = (byte) 13;
    public static final byte ON = (byte) 18;
    public static final byte PDF = (byte) 7;
    public static final byte f110R = (byte) 3;
    public static final byte RLE = (byte) 5;
    public static final byte RLO = (byte) 6;
    public static final byte f111S = (byte) 16;
    public static final byte TYPE_MAX = (byte) 18;
    public static final byte TYPE_MIN = (byte) 0;
    public static final byte WS = (byte) 17;
    private static char[] baseTypes = new char[]{'\u0000', '\b', '\u000e', '\t', '\t', '\u0010', '\n', '\n', '\u000f', '\u000b', '\u000b', '\u0010', '\f', '\f', '\u0011', '\r', '\r', '\u000f', '\u000e', '\u001b', '\u000e', '\u001c', '\u001e', '\u000f', '\u001f', '\u001f', '\u0010', ' ', ' ', '\u0011', '!', '\"', '\u0012', '#', '%', '\n', '&', '*', '\u0012', '+', '+', '\n', ',', ',', '\f', '-', '-', '\n', '.', '.', '\f', '/', '/', '\t', '0', '9', '\b', ':', ':', '\f', ';', '@', '\u0012', 'A', 'Z', '\u0000', '[', '`', '\u0012', 'a', 'z', '\u0000', '{', '~', '\u0012', Ascii.MAX, '', '\u000e', '', '', '\u000f', '', '', '\u000e', ' ', ' ', '\f', '¡', '¡', '\u0012', '¢', '¥', '\n', '¦', '©', '\u0012', 'ª', 'ª', '\u0000', '«', '¯', '\u0012', '°', '±', '\n', '²', '³', '\b', '´', '´', '\u0012', 'µ', 'µ', '\u0000', '¶', '¸', '\u0012', '¹', '¹', '\b', 'º', 'º', '\u0000', '»', '¿', '\u0012', 'À', 'Ö', '\u0000', '×', '×', '\u0012', 'Ø', 'ö', '\u0000', '÷', '÷', '\u0012', 'ø', 'ʸ', '\u0000', 'ʹ', 'ʺ', '\u0012', 'ʻ', 'ˁ', '\u0000', '˂', 'ˏ', '\u0012', 'ː', 'ˑ', '\u0000', '˒', '˟', '\u0012', 'ˠ', 'ˤ', '\u0000', '˥', '˭', '\u0012', 'ˮ', 'ˮ', '\u0000', '˯', '˿', '\u0012', '̀', '͗', '\r', '͘', '͜', '\u0000', '͝', 'ͯ', '\r', 'Ͱ', 'ͳ', '\u0000', 'ʹ', '͵', '\u0012', 'Ͷ', 'ͽ', '\u0000', ';', ';', '\u0012', 'Ϳ', '΃', '\u0000', '΄', '΅', '\u0012', 'Ά', 'Ά', '\u0000', '·', '·', '\u0012', 'Έ', 'ϵ', '\u0000', '϶', '϶', '\u0012', 'Ϸ', '҂', '\u0000', '҃', '҆', '\r', '҇', '҇', '\u0000', '҈', '҉', '\r', 'Ҋ', '։', '\u0000', '֊', '֊', '\u0012', '֋', '֐', '\u0000', '֑', '֡', '\r', '֢', '֢', '\u0000', '֣', 'ֹ', '\r', 'ֺ', 'ֺ', '\u0000', 'ֻ', 'ֽ', '\r', '־', '־', '\u0003', 'ֿ', 'ֿ', '\r', '׀', '׀', '\u0003', 'ׁ', 'ׂ', '\r', '׃', '׃', '\u0003', 'ׄ', 'ׄ', '\r', 'ׅ', '׏', '\u0000', 'א', 'ת', '\u0003', '׫', 'ׯ', '\u0000', 'װ', '״', '\u0003', '׵', '׿', '\u0000', '؀', '؃', '\u0004', '؄', '؋', '\u0000', '،', '،', '\f', '؍', '؍', '\u0004', '؎', '؏', '\u0012', 'ؐ', 'ؕ', '\r', 'ؖ', 'ؚ', '\u0000', '؛', '؛', '\u0004', '؜', '؞', '\u0000', '؟', '؟', '\u0004', 'ؠ', 'ؠ', '\u0000', 'ء', 'غ', '\u0004', 'ػ', 'ؿ', '\u0000', 'ـ', 'ي', '\u0004', 'ً', '٘', '\r', 'ٙ', 'ٟ', '\u0000', '٠', '٩', '\u000b', '٪', '٪', '\n', '٫', '٬', '\u000b', '٭', 'ٯ', '\u0004', 'ٰ', 'ٰ', '\r', 'ٱ', 'ە', '\u0004', 'ۖ', 'ۜ', '\r', '۝', '۝', '\u0004', '۞', 'ۤ', '\r', 'ۥ', 'ۦ', '\u0004', 'ۧ', 'ۨ', '\r', '۩', '۩', '\u0012', '۪', 'ۭ', '\r', 'ۮ', 'ۯ', '\u0004', '۰', '۹', '\b', 'ۺ', '܍', '\u0004', '܎', '܎', '\u0000', '܏', '܏', '\u000e', 'ܐ', 'ܐ', '\u0004', 'ܑ', 'ܑ', '\r', 'ܒ', 'ܯ', '\u0004', 'ܰ', '݊', '\r', '݋', '݌', '\u0000', 'ݍ', 'ݏ', '\u0004', 'ݐ', 'ݿ', '\u0000', 'ހ', 'ޥ', '\u0004', 'ަ', 'ް', '\r', 'ޱ', 'ޱ', '\u0004', '޲', 'ऀ', '\u0000', 'ँ', 'ं', '\r', 'ः', 'ऻ', '\u0000', '़', '़', '\r', 'ऽ', 'ी', '\u0000', 'ु', DevanagariLigaturizer.DEVA_MATRA_AI, '\r', 'ॉ', 'ौ', '\u0000', DevanagariLigaturizer.DEVA_HALANTA, DevanagariLigaturizer.DEVA_HALANTA, '\r', 'ॎ', 'ॐ', '\u0000', '॑', '॔', '\r', 'ॕ', 'ॡ', '\u0000', DevanagariLigaturizer.DEVA_MATRA_HLR, DevanagariLigaturizer.DEVA_MATRA_HLRR, '\r', '।', 'ঀ', '\u0000', 'ঁ', 'ঁ', '\r', 'ং', '঻', '\u0000', '়', '়', '\r', 'ঽ', 'ী', '\u0000', 'ু', 'ৄ', '\r', '৅', 'ৌ', '\u0000', '্', '্', '\r', 'ৎ', 'ৡ', '\u0000', 'ৢ', 'ৣ', '\r', '৤', 'ৱ', '\u0000', '৲', '৳', '\n', '৴', '਀', '\u0000', 'ਁ', 'ਂ', '\r', 'ਃ', '਻', '\u0000', '਼', '਼', '\r', '਽', 'ੀ', '\u0000', 'ੁ', 'ੂ', '\r', '੃', '੆', '\u0000', 'ੇ', 'ੈ', '\r', '੉', '੊', '\u0000', 'ੋ', '੍', '\r', '੎', '੯', '\u0000', 'ੰ', 'ੱ', '\r', 'ੲ', '઀', '\u0000', 'ઁ', 'ં', '\r', 'ઃ', '઻', '\u0000', '઼', '઼', '\r', 'ઽ', 'ી', '\u0000', 'ુ', 'ૅ', '\r', '૆', '૆', '\u0000', GujaratiLigaturizer.GUJR_MATRA_E, GujaratiLigaturizer.GUJR_MATRA_AI, '\r', 'ૉ', 'ૌ', '\u0000', GujaratiLigaturizer.GUJR_HALANTA, GujaratiLigaturizer.GUJR_HALANTA, '\r', '૎', 'ૡ', '\u0000', GujaratiLigaturizer.GUJR_MATRA_HLR, GujaratiLigaturizer.GUJR_MATRA_HLRR, '\r', '૤', '૰', '\u0000', '૱', '૱', '\n', '૲', '଀', '\u0000', 'ଁ', 'ଁ', '\r', 'ଂ', '଻', '\u0000', '଼', '଼', '\r', 'ଽ', 'ା', '\u0000', 'ି', 'ି', '\r', 'ୀ', 'ୀ', '\u0000', 'ୁ', 'ୃ', '\r', 'ୄ', 'ୌ', '\u0000', '୍', '୍', '\r', '୎', '୕', '\u0000', 'ୖ', 'ୖ', '\r', 'ୗ', '஁', '\u0000', 'ஂ', 'ஂ', '\r', 'ஃ', 'ி', '\u0000', 'ீ', 'ீ', '\r', 'ு', 'ௌ', '\u0000', '்', '்', '\r', '௎', '௲', '\u0000', '௳', '௸', '\u0012', '௹', '௹', '\n', '௺', '௺', '\u0012', '௻', 'ఽ', '\u0000', 'ా', 'ీ', '\r', 'ు', '౅', '\u0000', 'ె', 'ై', '\r', '౉', '౉', '\u0000', 'ొ', '్', '\r', '౎', '౔', '\u0000', 'ౕ', 'ౖ', '\r', '౗', '಻', '\u0000', '಼', '಼', '\r', 'ಽ', 'ೋ', '\u0000', 'ೌ', '್', '\r', '೎', 'ീ', '\u0000', 'ു', 'ൃ', '\r', 'ൄ', 'ൌ', '\u0000', '്', '്', '\r', 'ൎ', '෉', '\u0000', '්', '්', '\r', '෋', 'ෑ', '\u0000', 'ි', 'ු', '\r', '෕', '෕', '\u0000', 'ූ', 'ූ', '\r', '෗', 'ะ', '\u0000', 'ั', 'ั', '\r', 'า', 'ำ', '\u0000', 'ิ', 'ฺ', '\r', '฻', '฾', '\u0000', '฿', '฿', '\n', 'เ', 'ๆ', '\u0000', '็', '๎', '\r', '๏', 'ະ', '\u0000', 'ັ', 'ັ', '\r', 'າ', 'ຳ', '\u0000', 'ິ', 'ູ', '\r', '຺', '຺', '\u0000', 'ົ', 'ຼ', '\r', 'ຽ', '໇', '\u0000', '່', 'ໍ', '\r', '໎', '༗', '\u0000', '༘', '༙', '\r', '༚', '༴', '\u0000', '༵', '༵', '\r', '༶', '༶', '\u0000', '༷', '༷', '\r', '༸', '༸', '\u0000', '༹', '༹', '\r', '༺', '༽', '\u0012', '༾', '཰', '\u0000', 'ཱ', 'ཾ', '\r', 'ཿ', 'ཿ', '\u0000', 'ྀ', '྄', '\r', '྅', '྅', '\u0000', '྆', '྇', '\r', 'ྈ', 'ྏ', '\u0000', 'ྐ', 'ྗ', '\r', '྘', '྘', '\u0000', 'ྙ', 'ྼ', '\r', '྽', '࿅', '\u0000', '࿆', '࿆', '\r', '࿇', 'ာ', '\u0000', 'ိ', 'ူ', '\r', 'ေ', 'ေ', '\u0000', 'ဲ', 'ဲ', '\r', 'ဳ', 'ဵ', '\u0000', 'ံ', '့', '\r', 'း', 'း', '\u0000', '္', '္', '\r', '်', 'ၗ', '\u0000', 'ၘ', 'ၙ', '\r', 'ၚ', 'ᙿ', '\u0000', ' ', ' ', '\u0011', 'ᚁ', 'ᚚ', '\u0000', '᚛', '᚜', '\u0012', '᚝', 'ᜑ', '\u0000', 'ᜒ', '᜔', '\r', '᜕', 'ᜱ', '\u0000', 'ᜲ', '᜴', '\r', '᜵', 'ᝑ', '\u0000', 'ᝒ', 'ᝓ', '\r', '᝔', '᝱', '\u0000', 'ᝲ', 'ᝳ', '\r', '᝴', 'ា', '\u0000', 'ិ', 'ួ', '\r', 'ើ', 'ៅ', '\u0000', 'ំ', 'ំ', '\r', 'ះ', 'ៈ', '\u0000', '៉', '៓', '\r', '។', '៚', '\u0000', '៛', '៛', '\n', 'ៜ', 'ៜ', '\u0000', '៝', '៝', '\r', '៞', '៯', '\u0000', '៰', '៹', '\u0012', '៺', '៿', '\u0000', '᠀', '᠊', '\u0012', '᠋', '᠍', '\r', '᠎', '᠎', '\u0011', '᠏', 'ᢨ', '\u0000', 'ᢩ', 'ᢩ', '\r', 'ᢪ', '᤟', '\u0000', 'ᤠ', 'ᤢ', '\r', 'ᤣ', 'ᤦ', '\u0000', 'ᤧ', 'ᤫ', '\r', '᤬', 'ᤱ', '\u0000', 'ᤲ', 'ᤲ', '\r', 'ᤳ', 'ᤸ', '\u0000', '᤹', '᤻', '\r', '᤼', '᤿', '\u0000', '᥀', '᥀', '\u0012', '᥁', '᥃', '\u0000', '᥄', '᥅', '\u0012', '᥆', '᧟', '\u0000', '᧠', '᧿', '\u0012', 'ᨀ', 'ᾼ', '\u0000', '᾽', '᾽', '\u0012', 'ι', 'ι', '\u0000', '᾿', '῁', '\u0012', 'ῂ', 'ῌ', '\u0000', '῍', '῏', '\u0012', 'ῐ', '῜', '\u0000', '῝', '῟', '\u0012', 'ῠ', 'Ῥ', '\u0000', '῭', '`', '\u0012', '῰', 'ῼ', '\u0000', '´', '῾', '\u0012', '῿', '῿', '\u0000', ' ', ' ', '\u0011', '​', '‍', '\u000e', '‎', '‎', '\u0000', '‏', '‏', '\u0003', '‐', '‧', '\u0012', ' ', ' ', '\u0011', BaseFont.PARAGRAPH_SEPARATOR, BaseFont.PARAGRAPH_SEPARATOR, '\u000f', '‪', '‪', '\u0001', '‫', '‫', '\u0005', '‬', '‬', '\u0007', '‭', '‭', '\u0002', '‮', '‮', '\u0006', ' ', ' ', '\u0011', '‰', '‴', '\n', '‵', '⁔', '\u0012', '⁕', '⁖', '\u0000', '⁗', '⁗', '\u0012', '⁘', '⁞', '\u0000', ' ', ' ', '\u0011', '⁠', '⁣', '\u000e', '⁤', '⁩', '\u0000', '⁪', '⁯', '\u000e', '⁰', '⁰', '\b', 'ⁱ', '⁳', '\u0000', '⁴', '⁹', '\b', '⁺', '⁻', '\n', '⁼', '⁾', '\u0012', 'ⁿ', 'ⁿ', '\u0000', '₀', '₉', '\b', '₊', '₋', '\n', '₌', '₎', '\u0012', '₏', '₟', '\u0000', '₠', '₱', '\n', '₲', '⃏', '\u0000', '⃐', '⃪', '\r', '⃫', '⃿', '\u0000', '℀', '℁', '\u0012', 'ℂ', 'ℂ', '\u0000', '℃', '℆', '\u0012', 'ℇ', 'ℇ', '\u0000', '℈', '℉', '\u0012', 'ℊ', 'ℓ', '\u0000', '℔', '℔', '\u0012', 'ℕ', 'ℕ', '\u0000', '№', '℘', '\u0012', 'ℙ', 'ℝ', '\u0000', '℞', '℣', '\u0012', 'ℤ', 'ℤ', '\u0000', '℥', '℥', '\u0012', 'Ω', 'Ω', '\u0000', '℧', '℧', '\u0012', 'ℨ', 'ℨ', '\u0000', '℩', '℩', '\u0012', 'K', 'ℭ', '\u0000', '℮', '℮', '\n', 'ℯ', 'ℱ', '\u0000', 'Ⅎ', 'Ⅎ', '\u0012', 'ℳ', 'ℹ', '\u0000', '℺', '℻', '\u0012', 'ℼ', 'ℿ', '\u0000', '⅀', '⅄', '\u0012', 'ⅅ', 'ⅉ', '\u0000', '⅊', '⅋', '\u0012', '⅌', '⅒', '\u0000', '⅓', '⅟', '\u0012', 'Ⅰ', '↏', '\u0000', '←', '∑', '\u0012', '−', '∓', '\n', '∔', '⌵', '\u0012', '⌶', '⍺', '\u0000', '⍻', '⎔', '\u0012', '⎕', '⎕', '\u0000', '⎖', '⏐', '\u0012', '⏑', '⏿', '\u0000', '␀', '␦', '\u0012', '␧', '␿', '\u0000', '⑀', '⑊', '\u0012', '⑋', '⑟', '\u0000', '①', '⒛', '\b', '⒜', 'ⓩ', '\u0000', '⓪', '⓪', '\b', '⓫', '☗', '\u0012', '☘', '☘', '\u0000', '☙', '♽', '\u0012', '♾', '♿', '\u0000', '⚀', '⚑', '\u0012', '⚒', '⚟', '\u0000', '⚠', '⚡', '\u0012', '⚢', '✀', '\u0000', '✁', '✄', '\u0012', '✅', '✅', '\u0000', '✆', '✉', '\u0012', '✊', '✋', '\u0000', '✌', '✧', '\u0012', '✨', '✨', '\u0000', '✩', '❋', '\u0012', '❌', '❌', '\u0000', '❍', '❍', '\u0012', '❎', '❎', '\u0000', '❏', '❒', '\u0012', '❓', '❕', '\u0000', '❖', '❖', '\u0012', '❗', '❗', '\u0000', '❘', '❞', '\u0012', '❟', '❠', '\u0000', '❡', '➔', '\u0012', '➕', '➗', '\u0000', '➘', '➯', '\u0012', '➰', '➰', '\u0000', '➱', '➾', '\u0012', '➿', '⟏', '\u0000', '⟐', '⟫', '\u0012', '⟬', '⟯', '\u0000', '⟰', '⬍', '\u0012', '⬎', '⹿', '\u0000', '⺀', '⺙', '\u0012', '⺚', '⺚', '\u0000', '⺛', '⻳', '\u0012', '⻴', '⻿', '\u0000', '⼀', '⿕', '\u0012', '⿖', '⿯', '\u0000', '⿰', '⿻', '\u0012', '⿼', '⿿', '\u0000', '　', '　', '\u0011', '、', '〄', '\u0012', '々', '〇', '\u0000', '〈', '〠', '\u0012', '〡', '〩', '\u0000', '〪', '〯', '\r', '〰', '〰', '\u0012', '〱', '〵', '\u0000', '〶', '〷', '\u0012', '〸', '〼', '\u0000', '〽', '〿', '\u0012', '぀', '゘', '\u0000', '゙', '゚', '\r', '゛', '゜', '\u0012', 'ゝ', 'ゟ', '\u0000', '゠', '゠', '\u0012', 'ァ', 'ヺ', '\u0000', '・', '・', '\u0012', 'ー', '㈜', '\u0000', '㈝', '㈞', '\u0012', '㈟', '㉏', '\u0000', '㉐', '㉟', '\u0012', '㉠', '㉻', '\u0000', '㉼', '㉽', '\u0012', '㉾', '㊰', '\u0000', '㊱', '㊿', '\u0012', '㋀', '㋋', '\u0000', '㋌', '㋏', '\u0012', '㋐', '㍶', '\u0000', '㍷', '㍺', '\u0012', '㍻', '㏝', '\u0000', '㏞', '㏟', '\u0012', '㏠', '㏾', '\u0000', '㏿', '㏿', '\u0012', '㐀', '䶿', '\u0000', '䷀', '䷿', '\u0012', '一', '꒏', '\u0000', '꒐', '꓆', '\u0012', '꓇', '﬜', '\u0000', 'יִ', 'יִ', '\u0003', 'ﬞ', 'ﬞ', '\r', 'ײַ', 'ﬨ', '\u0003', '﬩', '﬩', '\n', 'שׁ', 'זּ', '\u0003', '﬷', '﬷', '\u0000', 'טּ', 'לּ', '\u0003', '﬽', '﬽', '\u0000', 'מּ', 'מּ', '\u0003', '﬿', '﬿', '\u0000', 'נּ', 'סּ', '\u0003', '﭂', '﭂', '\u0000', 'ףּ', 'פּ', '\u0003', '﭅', '﭅', '\u0000', 'צּ', 'ﭏ', '\u0003', 'ﭐ', 'ﮱ', '\u0004', '﮲', '﯒', '\u0000', 'ﯓ', 'ﴽ', '\u0004', '﴾', '﴿', '\u0012', '﵀', '﵏', '\u0000', 'ﵐ', 'ﶏ', '\u0004', '﶐', '﶑', '\u0000', 'ﶒ', 'ﷇ', '\u0004', '﷈', '﷯', '\u0000', 'ﷰ', '﷼', '\u0004', '﷽', '﷽', '\u0012', '﷾', '﷿', '\u0000', '︀', '️', '\r', '︐', '︟', '\u0000', '︠', '︣', '\r', '︤', '︯', '\u0000', '︰', '﹏', '\u0012', '﹐', '﹐', '\f', '﹑', '﹑', '\u0012', '﹒', '﹒', '\f', '﹓', '﹓', '\u0000', '﹔', '﹔', '\u0012', '﹕', '﹕', '\f', '﹖', '﹞', '\u0012', '﹟', '﹟', '\n', '﹠', '﹡', '\u0012', '﹢', '﹣', '\n', '﹤', '﹦', '\u0012', '﹧', '﹧', '\u0000', '﹨', '﹨', '\u0012', '﹩', '﹪', '\n', '﹫', '﹫', '\u0012', '﹬', '﹯', '\u0000', 'ﹰ', 'ﹴ', '\u0004', '﹵', '﹵', '\u0000', 'ﹶ', 'ﻼ', '\u0004', '﻽', '﻾', '\u0000', '﻿', '﻿', '\u000e', '＀', '＀', '\u0000', '！', '＂', '\u0012', '＃', '％', '\n', '＆', '＊', '\u0012', '＋', '＋', '\n', '，', '，', '\f', '－', '－', '\n', '．', '．', '\f', '／', '／', '\t', '０', '９', '\b', '：', '：', '\f', '；', '＠', '\u0012', 'Ａ', 'Ｚ', '\u0000', '［', '｀', '\u0012', 'ａ', 'ｚ', '\u0000', '｛', '･', '\u0012', 'ｦ', '￟', '\u0000', '￠', '￡', '\n', '￢', '￤', '\u0012', '￥', '￦', '\n', '￧', '￧', '\u0000', '￨', '￮', '\u0012', '￯', '￸', '\u0000', '￹', '￻', '\u000e', '￼', '�', '\u0012', '￾', '￿', '\u0000'};
    private static final byte[] rtypes = new byte[65536];
    private byte[] embeddings;
    private byte[] initialTypes;
    private byte paragraphEmbeddingLevel = (byte) -1;
    private byte[] resultLevels;
    private byte[] resultTypes;
    private int textLength;

    public BidiOrder(byte[] types) {
        validateTypes(types);
        this.initialTypes = (byte[]) types.clone();
        runAlgorithm();
    }

    public BidiOrder(byte[] types, byte paragraphEmbeddingLevel) {
        validateTypes(types);
        validateParagraphEmbeddingLevel(paragraphEmbeddingLevel);
        this.initialTypes = (byte[]) types.clone();
        this.paragraphEmbeddingLevel = paragraphEmbeddingLevel;
        runAlgorithm();
    }

    public BidiOrder(char[] text, int offset, int length, byte paragraphEmbeddingLevel) {
        this.initialTypes = new byte[length];
        for (int k = 0; k < length; k++) {
            this.initialTypes[k] = rtypes[text[offset + k]];
        }
        validateParagraphEmbeddingLevel(paragraphEmbeddingLevel);
        this.paragraphEmbeddingLevel = paragraphEmbeddingLevel;
        runAlgorithm();
    }

    public static final byte getDirection(char c) {
        return rtypes[c];
    }

    private void runAlgorithm() {
        this.textLength = this.initialTypes.length;
        this.resultTypes = (byte[]) this.initialTypes.clone();
        if (this.paragraphEmbeddingLevel == (byte) -1) {
            determineParagraphEmbeddingLevel();
        }
        this.resultLevels = new byte[this.textLength];
        setLevels(0, this.textLength, this.paragraphEmbeddingLevel);
        determineExplicitEmbeddingLevels();
        this.textLength = removeExplicitCodes();
        byte prevLevel = this.paragraphEmbeddingLevel;
        int limit;
        for (int start = 0; start < this.textLength; start = limit) {
            byte level = this.resultLevels[start];
            byte prevType = typeForLevel(Math.max(prevLevel, level));
            limit = start + 1;
            while (limit < this.textLength && this.resultLevels[limit] == level) {
                limit++;
            }
            byte succType = typeForLevel(Math.max(limit < this.textLength ? this.resultLevels[limit] : this.paragraphEmbeddingLevel, level));
            resolveWeakTypes(start, limit, level, prevType, succType);
            resolveNeutralTypes(start, limit, level, prevType, succType);
            resolveImplicitLevels(start, limit, level, prevType, succType);
            prevLevel = level;
        }
        this.textLength = reinsertExplicitCodes(this.textLength);
    }

    private void determineParagraphEmbeddingLevel() {
        byte strongType = (byte) -1;
        for (int i = 0; i < this.textLength; i++) {
            byte t = this.resultTypes[i];
            if (t == (byte) 0 || t == (byte) 4 || t == (byte) 3) {
                strongType = t;
                break;
            }
        }
        if (strongType == (byte) -1) {
            this.paragraphEmbeddingLevel = (byte) 0;
        } else if (strongType == (byte) 0) {
            this.paragraphEmbeddingLevel = (byte) 0;
        } else {
            this.paragraphEmbeddingLevel = (byte) 1;
        }
    }

    private void determineExplicitEmbeddingLevels() {
        this.embeddings = processEmbeddings(this.resultTypes, this.paragraphEmbeddingLevel);
        for (int i = 0; i < this.textLength; i++) {
            byte level = this.embeddings[i];
            if ((level & 128) != 0) {
                level = (byte) (level & 127);
                this.resultTypes[i] = typeForLevel(level);
            }
            this.resultLevels[i] = level;
        }
    }

    private int removeExplicitCodes() {
        int w = 0;
        for (int i = 0; i < this.textLength; i++) {
            byte t = this.initialTypes[i];
            if (!(t == (byte) 1 || t == (byte) 5 || t == (byte) 2 || t == (byte) 6 || t == (byte) 7 || t == (byte) 14)) {
                this.embeddings[w] = this.embeddings[i];
                this.resultTypes[w] = this.resultTypes[i];
                this.resultLevels[w] = this.resultLevels[i];
                w++;
            }
        }
        return w;
    }

    private int reinsertExplicitCodes(int textLength) {
        int i = this.initialTypes.length;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            byte t = this.initialTypes[i];
            if (t == (byte) 1 || t == (byte) 5 || t == (byte) 2 || t == (byte) 6 || t == (byte) 7 || t == (byte) 14) {
                this.embeddings[i] = (byte) 0;
                this.resultTypes[i] = t;
                this.resultLevels[i] = (byte) -1;
            } else {
                textLength--;
                this.embeddings[i] = this.embeddings[textLength];
                this.resultTypes[i] = this.resultTypes[textLength];
                this.resultLevels[i] = this.resultLevels[textLength];
            }
        }
        if (this.resultLevels[0] == (byte) -1) {
            this.resultLevels[0] = this.paragraphEmbeddingLevel;
        }
        for (i = 1; i < this.initialTypes.length; i++) {
            if (this.resultLevels[i] == (byte) -1) {
                this.resultLevels[i] = this.resultLevels[i - 1];
            }
        }
        return this.initialTypes.length;
    }

    private static byte[] processEmbeddings(byte[] resultTypes, byte paragraphEmbeddingLevel) {
        int textLength = resultTypes.length;
        byte[] embeddings = new byte[textLength];
        byte[] embeddingValueStack = new byte[62];
        int stackCounter = 0;
        int overflowAlmostCounter = 0;
        int overflowCounter = 0;
        byte currentEmbeddingLevel = paragraphEmbeddingLevel;
        byte currentEmbeddingValue = paragraphEmbeddingLevel;
        for (int i = 0; i < textLength; i++) {
            embeddings[i] = currentEmbeddingValue;
            byte t = resultTypes[i];
            switch (t) {
                case (byte) 1:
                case (byte) 2:
                case (byte) 5:
                case (byte) 6:
                    if (overflowCounter == 0) {
                        byte newLevel;
                        if (t == (byte) 5 || t == (byte) 6) {
                            newLevel = (byte) ((currentEmbeddingLevel + 1) | 1);
                        } else {
                            newLevel = (byte) ((currentEmbeddingLevel + 2) & -2);
                        }
                        if (newLevel >= DocWriter.GT) {
                            if (currentEmbeddingLevel == (byte) 60) {
                                overflowAlmostCounter++;
                                break;
                            }
                        }
                        embeddingValueStack[stackCounter] = currentEmbeddingValue;
                        stackCounter++;
                        currentEmbeddingLevel = newLevel;
                        if (t == (byte) 2 || t == (byte) 6) {
                            currentEmbeddingValue = (byte) (newLevel | 128);
                        } else {
                            currentEmbeddingValue = newLevel;
                        }
                        embeddings[i] = currentEmbeddingValue;
                        break;
                    }
                    overflowCounter++;
                    break;
                case (byte) 7:
                    if (overflowCounter <= 0) {
                        if (overflowAlmostCounter <= 0 || currentEmbeddingLevel == (byte) 61) {
                            if (stackCounter <= 0) {
                                break;
                            }
                            stackCounter--;
                            currentEmbeddingValue = embeddingValueStack[stackCounter];
                            currentEmbeddingLevel = (byte) (currentEmbeddingValue & 127);
                            break;
                        }
                        overflowAlmostCounter--;
                        break;
                    }
                    overflowCounter--;
                    break;
                    break;
                case (byte) 15:
                    stackCounter = 0;
                    overflowCounter = 0;
                    overflowAlmostCounter = 0;
                    currentEmbeddingLevel = paragraphEmbeddingLevel;
                    currentEmbeddingValue = paragraphEmbeddingLevel;
                    embeddings[i] = paragraphEmbeddingLevel;
                    break;
                default:
                    break;
            }
        }
        return embeddings;
    }

    private void resolveWeakTypes(int start, int limit, byte level, byte sor, byte eor) {
        int i;
        int j;
        byte preceedingCharacterType = sor;
        for (i = start; i < limit; i++) {
            byte t = this.resultTypes[i];
            if (t == (byte) 13) {
                this.resultTypes[i] = preceedingCharacterType;
            } else {
                preceedingCharacterType = t;
            }
        }
        for (i = start; i < limit; i++) {
            if (this.resultTypes[i] == (byte) 8) {
                j = i - 1;
                while (j >= start) {
                    t = this.resultTypes[j];
                    if (t != (byte) 0 && t != (byte) 3 && t != (byte) 4) {
                        j--;
                    } else if (t == (byte) 4) {
                        this.resultTypes[i] = (byte) 11;
                    }
                }
            }
        }
        for (i = start; i < limit; i++) {
            if (this.resultTypes[i] == (byte) 4) {
                this.resultTypes[i] = (byte) 3;
            }
        }
        i = start + 1;
        while (i < limit - 1) {
            if (this.resultTypes[i] == (byte) 9 || this.resultTypes[i] == (byte) 12) {
                byte prevSepType = this.resultTypes[i - 1];
                byte succSepType = this.resultTypes[i + 1];
                if (prevSepType == (byte) 8 && succSepType == (byte) 8) {
                    this.resultTypes[i] = (byte) 8;
                } else if (this.resultTypes[i] == (byte) 12 && prevSepType == (byte) 11 && succSepType == (byte) 11) {
                    this.resultTypes[i] = (byte) 11;
                }
            }
            i++;
        }
        i = start;
        while (i < limit) {
            if (this.resultTypes[i] == (byte) 10) {
                int runstart = i;
                int runlimit = findRunLimit(runstart, limit, new byte[]{(byte) 10});
                t = runstart == start ? sor : this.resultTypes[runstart - 1];
                if (t != (byte) 8) {
                    if (runlimit == limit) {
                        t = eor;
                    } else {
                        t = this.resultTypes[runlimit];
                    }
                }
                if (t == (byte) 8) {
                    setTypes(runstart, runlimit, (byte) 8);
                }
                i = runlimit;
            }
            i++;
        }
        for (i = start; i < limit; i++) {
            t = this.resultTypes[i];
            if (t == (byte) 9 || t == (byte) 10 || t == (byte) 12) {
                this.resultTypes[i] = (byte) 18;
            }
        }
        for (i = start; i < limit; i++) {
            if (this.resultTypes[i] == (byte) 8) {
                byte prevStrongType = sor;
                for (j = i - 1; j >= start; j--) {
                    t = this.resultTypes[j];
                    if (t == (byte) 0 || t == (byte) 3) {
                        prevStrongType = t;
                        break;
                    }
                }
                if (prevStrongType == (byte) 0) {
                    this.resultTypes[i] = (byte) 0;
                }
            }
        }
    }

    private void resolveNeutralTypes(int start, int limit, byte level, byte sor, byte eor) {
        int i = start;
        while (i < limit) {
            byte t = this.resultTypes[i];
            if (t == (byte) 17 || t == (byte) 18 || t == (byte) 15 || t == (byte) 16) {
                byte leadingType;
                byte trailingType;
                byte resolvedType;
                int runstart = i;
                int runlimit = findRunLimit(runstart, limit, new byte[]{(byte) 15, (byte) 16, (byte) 17, (byte) 18});
                if (runstart == start) {
                    leadingType = sor;
                } else {
                    leadingType = this.resultTypes[runstart - 1];
                    if (!(leadingType == (byte) 0 || leadingType == (byte) 3)) {
                        if (leadingType == (byte) 11) {
                            leadingType = (byte) 3;
                        } else if (leadingType == (byte) 8) {
                            leadingType = (byte) 3;
                        }
                    }
                }
                if (runlimit == limit) {
                    trailingType = eor;
                } else {
                    trailingType = this.resultTypes[runlimit];
                    if (!(trailingType == (byte) 0 || trailingType == (byte) 3)) {
                        if (trailingType == (byte) 11) {
                            trailingType = (byte) 3;
                        } else if (trailingType == (byte) 8) {
                            trailingType = (byte) 3;
                        }
                    }
                }
                if (leadingType == trailingType) {
                    resolvedType = leadingType;
                } else {
                    resolvedType = typeForLevel(level);
                }
                setTypes(runstart, runlimit, resolvedType);
                i = runlimit;
            }
            i++;
        }
    }

    private void resolveImplicitLevels(int start, int limit, byte level, byte sor, byte eor) {
        int i;
        byte[] bArr;
        if ((level & 1) == 0) {
            for (i = start; i < limit; i++) {
                byte t = this.resultTypes[i];
                if (t != (byte) 0) {
                    if (t == (byte) 3) {
                        bArr = this.resultLevels;
                        bArr[i] = (byte) (bArr[i] + 1);
                    } else {
                        bArr = this.resultLevels;
                        bArr[i] = (byte) (bArr[i] + 2);
                    }
                }
            }
            return;
        }
        for (i = start; i < limit; i++) {
            if (this.resultTypes[i] != (byte) 3) {
                bArr = this.resultLevels;
                bArr[i] = (byte) (bArr[i] + 1);
            }
        }
    }

    public byte[] getLevels() {
        return getLevels(new int[]{this.textLength});
    }

    public byte[] getLevels(int[] linebreaks) {
        validateLineBreaks(linebreaks, this.textLength);
        byte[] result = (byte[]) this.resultLevels.clone();
        for (int i = 0; i < result.length; i++) {
            int j;
            byte t = this.initialTypes[i];
            if (t == (byte) 15 || t == (byte) 16) {
                result[i] = this.paragraphEmbeddingLevel;
                j = i - 1;
                while (j >= 0 && isWhitespace(this.initialTypes[j])) {
                    result[j] = this.paragraphEmbeddingLevel;
                    j--;
                }
            }
        }
        int start = 0;
        for (int limit : linebreaks) {
            j = limit - 1;
            while (j >= start && isWhitespace(this.initialTypes[j])) {
                result[j] = this.paragraphEmbeddingLevel;
                j--;
            }
            start = limit;
        }
        return result;
    }

    public int[] getReordering(int[] linebreaks) {
        validateLineBreaks(linebreaks, this.textLength);
        return computeMultilineReordering(getLevels(linebreaks), linebreaks);
    }

    private static int[] computeMultilineReordering(byte[] levels, int[] linebreaks) {
        int[] result = new int[levels.length];
        int start = 0;
        for (int limit : linebreaks) {
            byte[] templevels = new byte[(limit - start)];
            System.arraycopy(levels, start, templevels, 0, templevels.length);
            int[] temporder = computeReordering(templevels);
            for (int j = 0; j < temporder.length; j++) {
                result[start + j] = temporder[j] + start;
            }
            start = limit;
        }
        return result;
    }

    private static int[] computeReordering(byte[] levels) {
        int i;
        int[] result = new int[lineLength];
        for (i = 0; i < lineLength; i++) {
            result[i] = i;
        }
        byte highestLevel = (byte) 0;
        byte lowestOddLevel = (byte) 63;
        for (byte level : levels) {
            if (level > highestLevel) {
                highestLevel = level;
            }
            if ((level & 1) != 0 && level < lowestOddLevel) {
                lowestOddLevel = level;
            }
        }
        byte level2 = highestLevel;
        while (level2 >= lowestOddLevel) {
            i = 0;
            while (i < lineLength) {
                if (levels[i] >= level2) {
                    int start = i;
                    int limit = i + 1;
                    while (limit < lineLength && levels[limit] >= level2) {
                        limit++;
                    }
                    int j = start;
                    for (int k = limit - 1; j < k; k--) {
                        int temp = result[j];
                        result[j] = result[k];
                        result[k] = temp;
                        j++;
                    }
                    i = limit;
                }
                i++;
            }
            level2--;
        }
        return result;
    }

    public byte getBaseLevel() {
        return this.paragraphEmbeddingLevel;
    }

    private static boolean isWhitespace(byte biditype) {
        switch (biditype) {
            case (byte) 1:
            case (byte) 2:
            case (byte) 5:
            case (byte) 6:
            case (byte) 7:
            case (byte) 14:
            case (byte) 17:
                return true;
            default:
                return false;
        }
    }

    private static byte typeForLevel(int level) {
        return (level & 1) == 0 ? (byte) 0 : (byte) 3;
    }

    private int findRunLimit(int index, int limit, byte[] validSet) {
        index--;
        while (true) {
            index++;
            if (index >= limit) {
                return limit;
            }
            byte t = this.resultTypes[index];
            int i = 0;
            while (i < validSet.length) {
                if (t != validSet[i]) {
                    i++;
                }
            }
            return index;
        }
    }

    private int findRunStart(int index, byte[] validSet) {
        while (true) {
            index--;
            if (index < 0) {
                return 0;
            }
            byte t = this.resultTypes[index];
            int i = 0;
            while (i < validSet.length) {
                if (t != validSet[i]) {
                    i++;
                }
            }
            return index + 1;
        }
    }

    private void setTypes(int start, int limit, byte newType) {
        for (int i = start; i < limit; i++) {
            this.resultTypes[i] = newType;
        }
    }

    private void setLevels(int start, int limit, byte newLevel) {
        for (int i = start; i < limit; i++) {
            this.resultLevels[i] = newLevel;
        }
    }

    private static void validateTypes(byte[] types) {
        if (types == null) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("types.is.null", new Object[0]));
        }
        int i = 0;
        while (i < types.length) {
            if (types[i] < (byte) 0 || types[i] > (byte) 18) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.type.value.at.1.2", String.valueOf(i), String.valueOf(types[i])));
            }
            i++;
        }
        for (i = 0; i < types.length - 1; i++) {
            if (types[i] == (byte) 15) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("b.type.before.end.of.paragraph.at.index.1", i));
            }
        }
    }

    private static void validateParagraphEmbeddingLevel(byte paragraphEmbeddingLevel) {
        if (paragraphEmbeddingLevel != (byte) -1 && paragraphEmbeddingLevel != (byte) 0 && paragraphEmbeddingLevel != (byte) 1) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.paragraph.embedding.level.1", (int) paragraphEmbeddingLevel));
        }
    }

    private static void validateLineBreaks(int[] linebreaks, int textLength) {
        int prev = 0;
        int i = 0;
        while (i < linebreaks.length) {
            int next = linebreaks[i];
            if (next <= prev) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bad.linebreak.1.at.index.2", String.valueOf(next), String.valueOf(i)));
            } else {
                prev = next;
                i++;
            }
        }
        if (prev != textLength) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("last.linebreak.must.be.at.1", textLength));
        }
    }

    static {
        int k = 0;
        while (k < baseTypes.length) {
            int start = baseTypes[k];
            k++;
            int end = baseTypes[k];
            k++;
            byte b = (byte) baseTypes[k];
            int start2 = start;
            while (start2 <= end) {
                start = start2 + 1;
                rtypes[start2] = b;
                start2 = start;
            }
            k++;
        }
    }
}
