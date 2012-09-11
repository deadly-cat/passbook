package home.ingvar.passbook.dao.h2;

import java.io.UnsupportedEncodingException;

import org.h2.security.BlockCipher;
import org.h2.security.CipherFactory;
import org.h2.security.SHA256;
import org.h2.util.MathUtils;
import org.h2.util.Utils;

public abstract class StoredProcedure {
	
	public static byte[] hash(String password, String salt) throws UnsupportedEncodingException {
		byte[] hash = (password+salt).getBytes("UTF-8");
		for(int i = 0; i < 1000; i++) {
			hash = SHA256.getHash(hash, false);
		}
		return hash;
	}
	
	public static byte[] encrypt(String key, String data) throws UnsupportedEncodingException {
		byte[] k = key.getBytes("UTF-8");
		byte[] d = data.getBytes("UTF-8");
		
		BlockCipher cipher = CipherFactory.getBlockCipher("AES");
        byte[] newKey = getPaddedArrayCopy(k, cipher.getKeyLength());
        cipher.setKey(newKey);
        byte[] newData = getPaddedArrayCopy(d, BlockCipher.ALIGN);
        cipher.encrypt(newData, 0, newData.length);
        return newData;
	}
	
	public static String decrypt(String key, byte[] data) throws UnsupportedEncodingException {
		byte[] k = key.getBytes("UTF-8");
		
		BlockCipher cipher = CipherFactory.getBlockCipher("AES");
        byte[] newKey = getPaddedArrayCopy(k, cipher.getKeyLength());
        cipher.setKey(newKey);
        byte[] newData = getPaddedArrayCopy(data, BlockCipher.ALIGN);
        cipher.decrypt(newData, 0, newData.length);
        return new String(newData);
	}
	
	private static byte[] getPaddedArrayCopy(byte[] data, int blockSize) {
        int size = MathUtils.roundUpInt(data.length, blockSize);
        byte[] newData = Utils.newBytes(size);
        System.arraycopy(data, 0, newData, 0, data.length);
        return newData;
    }
	
}
