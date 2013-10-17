#pragma once

#include "Mediator.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace mediator
			{
				namespace example1
				{
					///
					/// <summary> * ͬ����ĳ����� </summary>
					/// 
					class Colleague
					{
					///	
					///	 <summary> * �����н��߶���ÿһ��ͬ���඼֪�������н��߶��� </summary>
					///	 
					private:
						Mediator *mediator;
					///	
					///	 <summary> * ���췽���������н��߶��� </summary>
					///	 * <param name="mediator"> �н��߶��� </param>
					///	 
					public:
						Colleague(Mediator *mediator);
					///	
					///	 <summary> * ��ȡ��ǰͬ�����Ӧ���н��߶��� </summary>
					///	 * <returns> ��Ӧ���н��߶��� </returns>
					///	 
						virtual Mediator *getMediator();
					};
				}
			}
		}
	}
}