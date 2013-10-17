#pragma once

#include "VoteState.h"
#include "NormalVoteState.h"
#include "RepeatVoteState.h"
#include "SpiteVoteState.h"
#include "BlackWarnVoteState.h"
#include "BlackVoteState.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace state
			{
				namespace example3
				{
					///
					/// <summary> * ͶƱ���� </summary>
					/// 
					class VoteManager2
					{
					///	
					///	 <summary> * ����״̬������� </summary>
					///	 
					private:
						VoteState *state;
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
								mapVote = std::map<std::string, std::string>();
								mapVoteCount = std::map<std::string, int>();

								initialized = true;
							}
						}

public:
	VoteManager2()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}