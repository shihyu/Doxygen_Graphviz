#pragma once

#include "Colleague.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace mediator
			{
				namespace example2
				{
					///
					/// <summary> * �н��߶���Ľӿ� </summary>
					/// 
					class Mediator
					{
					///	
					///	 <summary> * ͬ�¶���������ı��ʱ����֪ͨ�н��ߵķ�����
					///	 * ���н���ȥ������Ӧ��������ͬ�¶���Ľ��� </summary>
					///	 * <param name="colleague"> ͬ�¶������������н��߶���ͨ������ʵ��
					///	 *                  ȥ��ȡͬ�¶����״̬ </param>
					///	 
					public:
						virtual public void changed(Colleague*) = 0;
					};


				}
			}
		}
	}
}