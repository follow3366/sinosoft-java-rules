public class xxxService {
	public void checkright() throws Exception{
		throw new Exception();
		}
	public void check() throws Exception { // Noncompliant {{Exception should catch in controller}}
		try {

		} catch (Excepton e) { 
		}
	}
}

public interface InsuranceWebServicePortService extends javax.xml.rpc.Service {
    public java.lang.String getInsuranceWebServiceAddress();

    public com.ebao.health.insurance.InsuranceWebServicePort getInsuranceWebService() throws javax.xml.rpc.ServiceException;

    public com.ebao.health.insurance.InsuranceWebServicePort getInsuranceWebService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}