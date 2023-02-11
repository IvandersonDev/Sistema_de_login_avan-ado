package Model;
import java.util.List;
public class Authorization {

    private List<String> roles;

    public Authorization(List<String> roles) {
        this.roles = roles;
    }

    public boolean hasPermission(String resource, String action) {
        // Verifica se o usuário tem a permissão para acessar o recurso e realizar a ação específica
        for (String role : roles) {
            if (role.equals("admin") && resource.equals("admin")) {
                return true;
            } else if (role.equals("user") && resource.equals("user")) {
                return true;
            }
        }
        return false;
    }
}
