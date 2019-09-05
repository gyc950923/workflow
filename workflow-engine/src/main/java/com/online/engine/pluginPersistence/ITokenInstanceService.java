package com.online.engine.pluginPersistence;

import java.util.List;

import com.online.engine.Instance.IToken;

public interface ITokenInstanceService {

	void saveOrUpdateToken(IToken Token);

	void deleteTokensForNode(String processInstanceId, String nodeId);
	
	void deleteTokenById(String id);

	List<IToken> getTokensForProcessInstance(String processInstanceId,
			String nodeId);

	Integer getAliveTokensCountForProcessInst(String processInstanceId);

}
