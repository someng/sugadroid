package com.excilys.sugadroid.services.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLSocket;

import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * This class is a decorated SSLSocketFactory that allow any SSL certificate. It
 * extends SSLSocketFactory only for manipulating purposes, and does not use any
 * of its mother class code. However, it works almost as SSLSocketFactory, by
 * delegating calls to a javax.net.ssl.SSLSocketFactory
 * 
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class AllowingAllSSLSocketFactory extends SSLSocketFactory {

	private final javax.net.ssl.SSLSocketFactory delegate;
	private final HostNameResolver nameResolver = null;

	private X509HostnameVerifier hostnameVerifier;

	public AllowingAllSSLSocketFactory(javax.net.ssl.SSLSocketFactory delegate)
			throws KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, UnrecoverableKeyException {
		super(null);

		this.delegate = delegate;

		hostnameVerifier = new AllowAllHostnameVerifier();

	}

	@Override
	public Socket connectSocket(Socket sock, String host, int port,
			InetAddress localAddress, int localPort, HttpParams params)
			throws IOException {

		if (host == null) {
			throw new IllegalArgumentException("Target host may not be null.");
		}
		if (params == null) {
			throw new IllegalArgumentException("Parameters may not be null.");
		}

		SSLSocket sslsock = (SSLSocket) (sock != null ? sock : createSocket());

		if (localAddress != null || localPort > 0) {

			// we need to bind explicitly
			if (localPort < 0) {
				localPort = 0; // indicates "any"
			}

			InetSocketAddress isa = new InetSocketAddress(localAddress,
					localPort);
			sslsock.bind(isa);
		}

		int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
		int soTimeout = HttpConnectionParams.getSoTimeout(params);

		InetSocketAddress remoteAddress;
		if (nameResolver != null) {
			remoteAddress = new InetSocketAddress(nameResolver.resolve(host),
					port);
		} else {
			remoteAddress = new InetSocketAddress(host, port);
		}

		sslsock.connect(remoteAddress, connTimeout);

		sslsock.setSoTimeout(soTimeout);
		try {
			hostnameVerifier.verify(host, sslsock);
			// verifyHostName() didn't blowup - good!
		} catch (IOException iox) {
			// close the socket before re-throwing the exception
			try {
				sslsock.close();
			} catch (Exception x) { /* ignore */
			}
			throw iox;
		}

		return sslsock;

	}

	@Override
	public Socket createSocket() throws IOException {
		return delegate.createSocket();
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		SSLSocket sslSocket = (SSLSocket) delegate.createSocket(socket, host,
				port, autoClose);
		hostnameVerifier.verify(host, sslSocket);
		// verifyHostName() didn't blowup - good!
		return sslSocket;

	}

	@Override
	public X509HostnameVerifier getHostnameVerifier() {
		return hostnameVerifier;
	}

	@Override
	public boolean isSecure(Socket sock) throws IllegalArgumentException {
		if (sock == null) {
			throw new IllegalArgumentException("Socket may not be null.");
		}
		// This instanceof check is in line with createSocket() above.
		if (!(sock instanceof SSLSocket)) {
			throw new IllegalArgumentException(
					"Socket not created by this factory.");
		}
		// This check is performed last since it calls the argument object.
		if (sock.isClosed()) {
			throw new IllegalArgumentException("Socket is closed.");
		}

		return true;
	}

	@Override
	public void setHostnameVerifier(X509HostnameVerifier hostnameVerifier) {
		this.hostnameVerifier = hostnameVerifier;
	}
}
