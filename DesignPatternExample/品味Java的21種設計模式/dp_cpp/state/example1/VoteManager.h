#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace state
			{
				namespace example1
				{
					///
					/// <summary> * ͶƱ���� </summary>
					/// 
					class VoteManager
					{
					///	
					///	 <summary> * ��¼�û�ͶƱ�Ľ��,Map<String,String>��ӦMap<�û�����,ͶƱ��ѡ��> </summary>
					///	 
					private:
						Map<std::string, std::string> *mapVote;
					///	
					///	 <summary> * ��¼�û�ͶƱ����,Map<String,Integer>��ӦMap<�û�����,ͶƱ�Ĵ���> </summary>
					///	 
						Map<std::string, int> *mapVoteCount;
					///	
					///	 <summary> * ͶƱ </summary>
					///	 * <param name="user"> ͶƱ�ˣ�Ϊ�˼򵥣������û����� </param>
					///	 * <param name="voteItem"> ͶƱ��ѡ�� </param>
					///	 
					public:
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