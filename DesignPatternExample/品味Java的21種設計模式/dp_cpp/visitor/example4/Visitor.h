#pragma once

#include "EnterpriseCustomer.h"
#include "PersonalCustomer.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example4
				{
					///
					/// <summary> * �����߽ӿ� </summary>
					/// 
					class Visitor
					{
					///	
					///	 <summary> * ������ҵ�ͻ����൱�ڸ���ҵ�ͻ���ӷ����ߵĹ��� </summary>
					///	 * <param name="ec"> ��ҵ�ͻ��Ķ��� </param>
					///	 
					public:
						virtual public void visitEnterpriseCustomer(EnterpriseCustomer*) = 0;
					///	
					///	 <summary> * ���ʸ��˿ͻ����൱�ڸ����˿ͻ���ӷ����ߵĹ��� </summary>
					///	 * <param name="pc"> ���˿ͻ��Ķ��� </param>
					///	 
						virtual public void visitPersonalCustomer(PersonalCustomer*) = 0;
					};


				}
			}
		}
	}
}