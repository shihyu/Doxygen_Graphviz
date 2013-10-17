#pragma once

#include "Composite.h"
#include "Leaf.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example5
				{
					///
					/// <summary> * ������϶���ṹ�ķ����߽ӿ� </summary>
					/// 
					class Visitor
					{
					///	
					///	 <summary> * ������϶����൱�ڸ���϶�����ӷ����ߵĹ��� </summary>
					///	 * <param name="composite"> ��϶��� </param>
					///	 
					public:
						virtual public void visitComposite(Composite*) = 0;
					///	
					///	 <summary> * ����Ҷ�Ӷ����൱�ڸ�Ҷ�Ӷ�����ӷ����ߵĹ��� </summary>
					///	 * <param name="leaf"> Ҷ�Ӷ��� </param>
					///	 
						virtual public void visitLeaf(Leaf*) = 0;
					};
				}
			}
		}
	}
}