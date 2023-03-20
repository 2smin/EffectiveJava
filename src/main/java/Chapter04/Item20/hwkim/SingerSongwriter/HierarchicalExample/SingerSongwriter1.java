package Chapter04.Item20.hwkim.SingerSongwriter.HierarchicalExample;

import java.applet.AudioClip;

public abstract class SingerSongwriter1{
    // 다중 상속이 불가 하기 때문에
    // Singer
    abstract AudioClip sing(AudioClip song);
    // Songwriter
    abstract AudioClip compose(int chartPosition);

    abstract AudioClip strum();
    abstract void actSensitive();
}
