//package com.midorlo.k9.model.security.mapper;
//
//import com.midorlo.k9.domain.security.Clearance;
//import org.springframework.http.HttpMethod;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//public class AuthorityInfoMapper {
//    public Map<String, Map<HttpMethod, Long>> toTree(Collection<Clearance> clearanceInfos) {
//        Map<String, Map<HttpMethod, Long>> tree = new HashMap<>();
//        for (Clearance clearance : clearanceInfos) {
//            Long       id          = clearance.getId();
//            String     servletPath = clearance.getServlet().getPath();
//            HttpMethod method      = clearance.getMethod();
//            if (!tree.containsKey(servletPath)) {
//                tree.put(servletPath, new HashMap<>());
//            }
//            Map<HttpMethod, Long> methodMap = tree.get(servletPath);
//            methodMap.put(method, id);
//        }
//        return tree;
//    }
//}
