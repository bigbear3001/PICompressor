package com.perhab.compression;

import static org.junit.Assert.*;

import java.io.IOException;

import lombok.Cleanup;

import org.junit.Test;

public class PIInputStreamTest {
	
	/**
	 * A few places of pi from http://members.shaw.ca/francislyster/pi/pistats/pibase16.pdf
	 */
	public static final String PI = "243f6a8885a308d313198a2e03707344a4093822299f31d0082efa98ec4e6c89452821e638d01377be"
			+ "5466cf34e90c6cc0ac29b7c97c50dd3f84d5b5b54709179216d5d98979fb1bd1310ba698dfb5ac2ffd72dbd01adfb7b8e1afed6a"
			+ "267e96ba7c9045f12c7f9924a19947b3916cf70801f2e2858efc16636920d871574e69a458fea3f4933d7e0d95748f728eb65871"
			+ "8bcd5882154aee7b54a41dc25a59b59c30d5392af26013c5d1b023286085f0ca417918b8db38ef8e79dcb0603a180e6c9e0e8bb0"
			+ "1e8a3ed71577c1bd314b2778af2fda55605c60e65525f3aa55ab945748986263e8144055ca396a2aab10b6b4cc5c341141e8cea1"
			+ "5486af7c72e993b3ee1411636fbc2a2ba9c55d741831f6ce5c3e169b87931eafd6ba336c24cf5c7a325381289586773b8f48986b"
			+ "4bb9afc4bfe81b6628219361d809ccfb21a991487cac605dec8032ef845d5de98575b1dc262302eb651b8823893e81d396acc50f"
			+ "6d6ff383f442392e0b4482a484200469c8f04a9e1f9b5e21c66842f6e96c9a670c9c61abd388f06a51a0d2d8542f68960fa728ab"
			+ "5133a36eef0b6c137a3be4ba3bf0507efb2a98a1f1651d39af017666ca593e82430e888cee8619456f9fb47d84a5c33b8b5ebee0"
			+ "6f75d885c12073401a449f56c16aa64ed3aa62363f77061bfedf72429b023d37d0d724d00a1248db0fead349f1c09b075372c980"
			+ "991b7b25d479d8f6e8def7e3fe501ab6794c3b976ce0bd04c006bac1a94fb6409f60c45e5c9ec2196a246368fb6faf3e6c53b513"
			+ "39b2eb3b52ec6f6dfc511f9b30952ccc814544af5ebd09bee3d004de334afd660f2807192e4bb3c0cba85745c8740fd20b5f39b9"
			+ "d3fbdb5579c0bd1a60320ad6a100c6402c7279679f25fefb1fa3cc8ea5e9f8db3222f83c7516dffd616b152f501ec8ad0552ab32"
			+ "3db5fafd23876053317b483e00df829e5c57bbca6f8ca01a87562edf1769dbd542a8f6287effc3ac6732c68c4f5573695b27b0bb"
			+ "ca58c8e1ffa35db8f011a010fa3d98fd2183b84afcb56c2dd1d35b9a53e479b6f84565d28e49bc4bfb9790e1ddf2daa4cb7e3362"
			+ "fb1341cee4c6e8ef20cada36774c01d07e9efe2bf11fb495dbda4dae909198eaad8e716b93d5a0d08ed1d0afc725e08e3c5b2f8e"
			+ "7594b78ff6e2fbf2122b648888b812900df01c4fad5ea0688fc31cd1cff191b3a8c1ad2f2f2218be0e1777ea752dfe8b021fa1e5"
			+ "a0cc0fb56f74e818acf3d6ce89e299b4a84fe0fd13e0b77cc43b81d2ada8d9165fa2668095770593cc7314211a1477e6ad206577"
			+ "b5fa86c75442f5fb9d35cfebcdaf0c7b3e89a0d6411bd3ae1e7e4900250e2d2071b35e226800bb57b8e0af2464369bf009b91e55"
			+ "63911d59dfa6aa78c14389d95a537f207d5ba202e5b9c5832603766295cfa911c819684e734a41b3472dca7b14a94a1b5100529a"
			+ "532915d60f573fbc9bc6e42b60a47681e6740008ba6fb5571be91ff296ec6b2a0dd915b6636521e7b9f9b6ff34052ec585566453"
			+ "b02d5da99f8fa108ba47996e85076a4b7a70e9b5b32944db75092ec4192623ad6ea6b049a7df7d9cee60b88fedb266ecaa8c7169"
			+ "9a17ff5664526cc2b19ee1193602a575094c29a0591340e4183a3e3f54989a5b429d656b8fe4d699f73fd6a1d29c07efe830f54d"
			+ "2d38e6f0255dc14cdd20868470eb266382e9c6021ecc5e09686b3f3ebaefc93c9718146b6a70a1687f358452a0e286b79c5305aa"
			+ "5007373e07841c7fdeae5c8e7d44ec5716f2b8b03ada37f0500c0df01c1f040200b3ffae0cf51a3cb574b225837a58dc0921bdd1"
			+ "9113f97ca92ff69432477322f547013ae5e58137c2dadcc8b576349af3dda7a94461460fd0030eecc8c73ea4751e41e238cd993b"
			+ "ea0e2f3280bba1183eb3314e548b384f6db9086f420d03f60a04bf2cb8129024977c795679b072bcaf89afde9a771fd9930810b3"
			+ "8bae12dccf3f2e5512721f2e6b7124501adde69f84cd877a5847187408da17bc9f9abce";
	
	@Test
	public void testReadFirstPlace() throws IOException {
		String firstPlace = readNPlaces(1);
		assertEquals(PI.substring(0, 1), firstPlace);
	}
	
	@Test
	public void testRead10Places() throws IOException {
		String places = readNPlaces(10);
		assertEquals(PI.substring(0, 10), places);
	}
	
	@Test
	public void testRead200Places() throws IOException {
		String places = readNPlaces(200);
		assertEquals(PI.subSequence(0, 200), places);
	}
	
	@Test
	public void testRead500Places() throws IOException {
		String places = readNPlaces(500);
		assertEquals(PI.subSequence(0, 500), places);
	}
	
	@Test
	public void testRead1000Places() throws IOException {
		String places = readNPlaces(1000);
		assertEquals(PI.subSequence(0, 1000), places);
	}
	
	private String readNPlaces(int n) throws IOException {
		@Cleanup
		PIInputStream pi = new PIInputStream();
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < n; i++) {
			res.append(String.format("%01x", pi.read()));
		}
		return res.toString();
	}
}
