package com.jpexs.images.apng;

import com.jpexs.images.apng.chunks.Chunk;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;

/**
 * PNG container - header and chunks.
 *
 * @author JPEXS
 */
public class Png {

    /**
     * PNG file signature bytes.
     */
    public static final byte[] SIGNATURE = new byte[]{
        (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47,
        (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A
    };

    private List<Chunk> chunks = new ArrayList<>();

    /**
     * Constructs an empty PNG container.
     */
    public Png() {

    }

    /**
     * Constructs a PNG container with the specified chunks.
     *
     * @param chunks the list of chunks to include
     */
    public Png(List<Chunk> chunks) {
        this.chunks = new ArrayList<>(chunks);
    }

    /**
     * Constructs a PNG container by reading from an input stream.
     *
     * @param is the input stream to read from
     * @throws IOException if an I/O error occurs or the PNG signature is
     *                     invalid
     */
    public Png(InputStream is) throws IOException {
        PngInputStream pis = new PngInputStream(is);
        byte[] signature = pis.readBytes(8);
        if (!Arrays.equals(signature, SIGNATURE)) {
            throw new IOException("Invalid PNG signature");
        }
        chunks = pis.readChunkList();
    }

    /**
     * Writes png to stream.
     *
     * @param os the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    public void writeTo(OutputStream os) throws IOException {
        PngOutputStream pos = new PngOutputStream(os);
        pos.write(SIGNATURE);
        for (Chunk chunk : chunks) {
            pos.writeChunk(chunk);
        }
    }

    /**
     * Reads a PNG from the input stream and prints its chunk information to
     * standard output.
     *
     * @param is the input stream to read from
     * @throws IOException if an I/O error occurs
     */
    public static void dumpPng(InputStream is) throws IOException {
        Png png = new Png(is);
        for (Chunk chunk : png.chunks) {
            System.out.println(chunk.toString());
        }
    }

    /**
     * Returns a copy of the list of chunks.
     *
     * @return a new list containing all chunks
     */
    public List<Chunk> getChunks() {
        return new ArrayList<>(chunks);
    }

    /**
     * Sets the list of chunks.
     *
     * @param chunks the list of chunks to set
     */
    public void setChunks(List<Chunk> chunks) {
        this.chunks = new ArrayList<>(chunks);
    }

    /**
     * Adds a chunk to the end of the chunk list.
     *
     * @param chunk the chunk to add
     */
    public void addChunk(Chunk chunk) {
        chunks.add(chunk);
    }

    /**
     * Adds all chunks from the specified collection to the end of the chunk
     * list.
     *
     * @param chunk the collection of chunks to add
     */
    public void addAllChunks(Collection<? extends Chunk> chunk) {
        chunks.addAll(chunk);
    }

    /**
     * Inserts all chunks from the specified collection at the specified
     * position.
     *
     * @param index the position at which to insert the first chunk
     * @param chunk the collection of chunks to add
     */
    public void addAllChunks(int index, Collection<? extends Chunk> chunk) {
        chunks.addAll(index, chunk);
    }

    /**
     * Inserts a chunk at the specified position.
     *
     * @param index the position at which to insert the chunk
     * @param chunk the chunk to add
     */
    public void addChunk(int index, Chunk chunk) {
        chunks.add(index, chunk);
    }

    /**
     * Returns the chunk at the specified index.
     *
     * @param index the index of the chunk to return
     * @return the chunk at the specified index
     */
    public Chunk getChunk(int index) {
        return chunks.get(index);
    }

    /**
     * Removes the chunk at the specified index.
     *
     * @param index the index of the chunk to remove
     * @return the removed chunk
     */
    public Chunk removeChunk(int index) {
        return chunks.remove(index);
    }

    /**
     * Removes all chunks from the PNG.
     */
    public void clearChunks() {
        chunks.clear();
    }

    /**
     * Returns the number of chunks in the PNG.
     *
     * @return the chunk count
     */
    public int getChunkCount() {
        return chunks.size();
    }

}
