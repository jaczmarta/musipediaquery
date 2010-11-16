package info.textgrid.noteeditor.musipediaquery;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class SoapAdapter {

	public static String soapSend(String queryString) {
		String resultString;		
		String q1collection = "Musipedia";
		String q2query = queryString;
		String q3keywords = "";
		String q123hash = "";
		// md5 hashing - Should equal
		// md5(your_musipedia_passwordq1collectionq2queryq3keywords)
		String digestString = ConstantsCollection.PASSWORD + "" + q1collection + "" + q2query
				+ "" + q3keywords;
		q123hash = md5HashString(q123hash, digestString);
		try {
			SoapObject request = new SoapObject(ConstantsCollection.SOAP2_NAMESPACE,
					ConstantsCollection.SOAP2_METHOD_NAME);
			//all properties MUST be there(number==10), but value may be "".
			request.addProperty("username", ConstantsCollection.LOGIN);
			request.addProperty("q123hash", q123hash);
			request.addProperty("q1collection", q1collection);
			request.addProperty("q2query", q2query);
			request.addProperty("q3keywords", q3keywords);
			request.addProperty("q4pitch", "");
			request.addProperty("q5rhythm", "");
			request.addProperty("q6items", ConstantsCollection.RESULT_COUNT + "");
			request.addProperty("q7offset", "");
			request.addProperty("q8categories", "");

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // put all required data into a soap
			// envelope
			envelope.encodingStyle = SoapSerializationEnvelope.ENC2001;
			envelope.setOutputSoapObject(request); // prepare request
			AndroidHttpTransport httpTransport = new AndroidHttpTransport(
					ConstantsCollection.SOAP2_URL);
			httpTransport.debug = true;
			try {
				// send request
				httpTransport.call(ConstantsCollection.SOAP2_ACTION, envelope);
			} catch (IOException e) {
				Log.v("IOException", e.toString());
				resultString = e.toString();
			} catch (XmlPullParserException e) {
				Log.v("XMLPullParserException", e.toString());
				resultString = e.toString();
			} 
			// get response
			SoapObject result = (SoapObject) envelope.getResponse(); 
			resultString = result.toString();
			// insert results into queryResultList
		} catch (IOException e) {
			Log.v("IOException", e.toString());
			resultString = e.toString();
		}
		return resultString;
	}
	
	private static String md5HashString(String q123hash, String digestString) {
		MessageDigest digest;
		try {
			digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(digestString.getBytes(), 0, digestString.length());
			q123hash = (new BigInteger(1, digest.digest()).toString(16));

		} catch (NoSuchAlgorithmException e1) {
			Log.v("NoSuchAlgorithmException", e1.toString());
		}
		return q123hash;
	}

}
