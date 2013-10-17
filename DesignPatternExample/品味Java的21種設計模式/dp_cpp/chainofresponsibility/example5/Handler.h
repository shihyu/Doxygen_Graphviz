#pragma once

#include "RequestModel.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace chainofresponsibility
			{
				namespace example5
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
					///	 <summary> * ͨ�õ��������� </summary>
					///	 * <param name="rm"> ͨ�õ�������� </param>
					///	 * <returns> �������Ҫ���صĶ��� </returns>
					///	 
						virtual object *handleRequest(RequestModel *rm);
					};

				}
			}
		}
	}
}