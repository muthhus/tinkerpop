package com.tinkerpop.gremlin.giraph.process.computer.util;

import com.tinkerpop.gremlin.util.Serializer;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class KryoWritable<T> implements WritableComparable<KryoWritable> {

    T t;

    public KryoWritable() {
    }

    public KryoWritable(final T t) {
        this();
        this.t = t;
    }

    public T get() {
        return this.t;
    }

    public void set(final T t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return this.t.toString();
    }

    @Override
    public void readFields(final DataInput input) throws IOException {
        try {
            final byte[] objectBytes = WritableUtils.readCompressedByteArray(input);
            this.t = (T) Serializer.deserializeObject(objectBytes);
        } catch (final ClassNotFoundException e) {
            throw new IOException(e.getMessage(), e);
        }
        //this.t = (T) Constants.KRYO.readClassAndObject(new Input(new ByteArrayInputStream(WritableUtils.readCompressedByteArray(input))));
    }

    @Override
    public void write(final DataOutput output) throws IOException {
        WritableUtils.writeCompressedByteArray(output, Serializer.serializeObject(this.t));
        /*final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Output out = new Output(outputStream);
        Constants.KRYO.writeClassAndObject(out, this.t);
        out.flush();
        WritableUtils.writeCompressedByteArray(output, outputStream.toByteArray());
        out.close();*/
    }

    @Override
    public int compareTo(final KryoWritable kryoWritable) {
        return this.t instanceof Comparable ? ((Comparable) this.t).compareTo(kryoWritable.get()) : 1;
    }
}
