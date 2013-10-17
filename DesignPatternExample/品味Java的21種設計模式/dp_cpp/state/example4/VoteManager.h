#pragma once

#include "VoteState.h"
#include "NormalVoteState.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace state
			{
				namespace example4
				{
					///
					/// <summary> * ͶƱ���� </summary>
					/// 
					class VoteManager
					{
					///	
					///	 <summary> * ��¼��ǰÿ���û���Ӧ��״̬�������ÿ���û���ǰ��״̬�ǲ�ͬ��
					///	 * Map<String,VoteState>��ӦMap<�û�����,��ǰ��Ӧ��״̬�������> </summary>
					///	 
					private:
						Map<std::string, VoteState*> *mapState;

					///	
					///	 <summary> * ��¼�û�ͶƱ�Ľ��,Map<String,String>��ӦMap<�û�����,ͶƱ��ѡ��> </summary>
					///	 
						Map<std::string, std::string> *mapVote;
					///	
					///	 <summary> * ��¼�û�ͶƱ����,Map<String,Integer>��ӦMap<�û�����,ͶƱ�Ĵ���> </summary>
					///	 
						Map<std::string, int> *mapVoteCount;


					///	
					///	 <summary> * ��ȡ��¼�û�ͶƱ�����Map </summary>
					///	 * <returns> ��¼�û�ͶƱ�����Map </returns>
					///	 
					public:
						virtual Map<std::string, std::string> *getMapVote();
					///	
					///	 <summary> * ��ȡ��¼ÿ���û���Ӧ��״̬��������Map </summary>
					///	 * <returns> ��¼ÿ���û���Ӧ��״̬��������Map </returns>
					///	 
						virtual Map<std::string, VoteState*> *getMapState();
					///	
					///	 <summary> * ��ȡ��¼ÿ���û���Ӧ��ͶƱ������Map </summary>
					///	 * <returns> ��¼ÿ���û���Ӧ��ͶƱ������Map </returns>
					///	 
						virtual Map<std::string, int> *getMapVoteCount();
					///	
					///	 <summary> * ͶƱ </summary>
					///	 * <param name="user"> ͶƱ�ˣ�Ϊ�˼򵥣������û����� </param>
					///	 * <param name="voteItem"> ͶƱ��ѡ�� </param>
					///	 
						virtual void vote(std::string user, std::string voteItem);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								mapState = std::map<std::string, VoteState*>();
								mapVote = std::map<std::string, std::string>();
								mapVoteCount = std::map<std::string, int>();

								initialized = true;
							}
						}

public:
	VoteManager()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}