#pragma once

#include "ConcreteElementA.h"
#include "ConcreteElementB.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example3
				{
					///
					/// <summary> * �����߽ӿ� </summary>
					/// 
					class Visitor
					{
					///	
					///	 <summary> * ����Ԫ��A���൱�ڸ�Ԫ��A��ӷ����ߵĹ��� </summary>
					///	 * <param name="elementA"> Ԫ��A�Ķ��� </param>
					///	 
					public:
						virtual public void visitConcreteElementA(ConcreteElementA*) = 0;
					///	
					///	 <summary> * ����Ԫ��B���൱�ڸ�Ԫ��B��ӷ����ߵĹ��� </summary>
					///	 * <param name="elementB"> Ԫ��B�Ķ��� </param>
					///	 
						virtual public void visitConcreteElementB(ConcreteElementB*) = 0;
					};


				}
			}
		}
	}
}