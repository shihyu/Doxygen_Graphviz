#pragma once

#include "VoteManager.h"

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
					/// <summary> * ��װһ��ͶƱ״̬��ص���Ϊ </summary>
					/// 
					class VoteState
					{
					///	
					///	 <summary> * ����״̬��Ӧ����Ϊ </summary>
					///	 * <param name="user"> ͶƱ�� </param>
					///	 * <param name="voteItem"> ͶƱ�� </param>
					///	 * <param name="voteManager"> ͶƱ�����ģ�������ʵ��״̬��Ӧ�Ĺ��ܴ����ʱ��
					///	 *                    ���Իص������ĵ����� </param>
					///	 
					public:
						virtual public void vote(std::string, std::string, VoteManager*) = 0;
					};
				}
			}
		}
	}
}