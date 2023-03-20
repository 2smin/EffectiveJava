package Chapter03.item13.hwkim;

public class HashTable implements Cloneable{
    private Entry[] buckets = new Entry[50];
    private int size = 0;

    public void put(Entry entry){
        buckets[size++] = entry;
    }

    public void printAll(){
        for (int i=0;i<size;i++){
            System.out.println(buckets[i].toString());
        }
    }

    private static class Entry{
        final Object key;
        Object value;
        Entry next;

        public Entry(final Object key, final Object value, final Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        Entry deepCopy(){
            Entry result = new Entry(key, value, next);
            for(Entry p = result; p.next != null; p=p.next){
                p.next = new Entry(p.next.key, p.next.value, p.next.next);
            }
            return result;
        }
    }

    @Override
    protected HashTable clone() {
        try {
            HashTable result = (HashTable) super.clone();
            result.buckets = new Entry[buckets.length];

            for (int i =0; i < buckets.length; i++){
                if(buckets[i] != null){
                    result.buckets[i] = buckets[i].deepCopy();
                }
            }

            return result;
        }catch (CloneNotSupportedException cloneNotSupportedException){
            throw new AssertionError();
        }
    }
}
