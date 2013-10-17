#pragma once

#include "Strategy.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace strategy
			{
				namespace example3
				{

					///
					/// <summary> * �����Ķ���ͨ�������һ������Ĳ��Զ��� </summary>
					/// 
					class Context
					{
					///	
					///	 <summary> * ����һ������Ĳ��Զ��� </summary>
					///	 
					private:
						Strategy *strategy;
					///	
					///	 <summary> * ���췽��������һ������Ĳ��Զ��� </summary>
					///	 * <param name="aStrategy"> ����Ĳ��Զ��� </param>
					///	 
					public:
						Context(Strategy *aStrategy);
					///	
					///	 <summary> * �����ĶԿͻ����ṩ�Ĳ����ӿڣ������в����ͷ���ֵ </summary>
					///	 
						virtual void contextInterface();

					};


				}
			}
		}
	}
}