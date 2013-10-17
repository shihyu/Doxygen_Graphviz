#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace chainofresponsibility
			{
				namespace example3
				{
					///
					/// <summary> * ����ְ�����Ľӿ� </summary>
					/// 
					class Handler
					{
					///	
					///	 <summary> * ������һ����������Ķ��� </summary>
					///	 
					protected:
						Handler *successor;
					///	
					///	 <summary> * ������һ����������Ķ��� </summary>
					///	 * <param name="successor"> ��һ����������Ķ��� </param>
					///	 
					public:
						virtual void setSuccessor(Handler *successor);
					///	
					///	 <summary> * ����۲ͷ��õ����� </summary>
					///	 * <param name="user"> ������ </param>
					///	 * <param name="fee"> �����Ǯ�� </param>
					///	 * <returns> �ɹ���ʧ�ܵľ���֪ͨ </returns>
					///	 
						virtual std::string handleFeeRequest(std::string user, double fee) = 0;
					};

				}
			}
		}
	}
}