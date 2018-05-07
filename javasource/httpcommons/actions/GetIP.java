// This file was generated by Mendix Modeler.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package httpcommons.actions;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import com.mendix.core.Core;
import com.mendix.m2ee.api.IMxRuntimeRequest;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;

public class GetIP extends CustomJavaAction<java.lang.String>
{
	public GetIP(IContext context)
	{
		super(context);
	}

	@Override
	public java.lang.String executeAction() throws Exception
	{
		// BEGIN USER CODE
		IContext ctx = getContext();
		if (ctx.getRuntimeRequest().isPresent()) {
			IMxRuntimeRequest req = ctx.getRuntimeRequest().get();

			// https://stackoverflow.com/questions/16558869/getting-ip-address-of-client
			String xForwardedForHeader = req.getHeader("X-Forwarded-For");
			if (xForwardedForHeader == null) {
				return req.getRemoteAddr();
			} else {
				// As of https://en.wikipedia.org/wiki/X-Forwarded-For
				// The general format of the field is: X-Forwarded-For: client,
				// proxy1, proxy2 ...
				// we only want the client
				String forwardIp = new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
				boolean validIP = checkIP(forwardIp);
				if (validIP) {
					return forwardIp;			
				} else {
					Core.getLogger("GetIP").error("Could not parse X-Forwarded-for header to an IP address. Header: "+forwardIp);
					return "";
				}
			}
		} else {
			return "";
		}
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@Override
	public java.lang.String toString()
	{
		return "GetIP";
	}

	// BEGIN EXTRA CODE
	public static final boolean checkIP(final String ip) {
		boolean isIPv4;
		try {
			final InetAddress inet = InetAddress.getByName(ip);
			isIPv4 = inet.getHostAddress().equals(ip) && (inet instanceof Inet4Address || inet instanceof Inet6Address);
		} catch (final UnknownHostException e) {
			isIPv4 = false;
		}
		return isIPv4;
	}
	// END EXTRA CODE
}
